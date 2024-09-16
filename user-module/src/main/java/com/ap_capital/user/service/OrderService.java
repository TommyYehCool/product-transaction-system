package com.ap_capital.user.service;

import com.ap_capital.common.feign_client.merchant_module.MerchantFeignClient;
import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.common.model.user_module.Order;
import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.merchant_module.merchant.CheckProductReq;
import com.ap_capital.common.req.merchant_module.merchant.ProductSoldReq;
import com.ap_capital.common.req.user_module.order.CreateOrderReq;
import com.ap_capital.common.resp.merchant_module.merchat.CheckProductResp;
import com.ap_capital.common.utils.UUIDUtils;
import com.ap_capital.user.mapper.OrderMapper;
import com.ap_capital.user.mapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderService {

    private final UserMapper userMapper;
    private final OrderMapper orderMapper;
    private final MerchantFeignClient merchantFeignClient;

    public OrderService(
            UserMapper userMapper,
            OrderMapper orderMapper,
            MerchantFeignClient merchantFeignClient
    ) {
        this.userMapper = userMapper;
        this.orderMapper = orderMapper;
        this.merchantFeignClient = merchantFeignClient;
    }

    @Transactional
    public void createOrder(Long userId, CreateOrderReq orderReq) {
        // 1. 查詢用戶
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // 2. 查詢產品庫存並計算出總金額
        CheckProductReq checkProductReq
                = CheckProductReq.builder()
                    .productSku(orderReq.getProductSku())
                    .quantity(orderReq.getQuantity())
                    .build();
        ResponseEntity<CheckProductResp> checkProductResp
                = merchantFeignClient.checkProduct(checkProductReq);

        CheckProductResp checkProductResult = checkProductResp.getBody();

        // 3. 檢查用戶的預付帳戶餘額是否足夠
        assert checkProductResult != null;
        if (user.getPrepaidAccountBalance().compareTo(checkProductResult.getTotalAmount()) < 0) {
            throw new RuntimeException("Insufficient balance in user's prepaid account.");
        }

        final String orderId = UUIDUtils.getUUID();

        // 4. 從用戶預付帳戶中扣除相應金額
        userMapper.paid(userId, checkProductResult.getTotalAmount(), new Date());

        // 5. 商戶模組處理商品賣出
        ProductSoldReq productSoldReq
                = ProductSoldReq.builder()
                    .merchantId(checkProductResult.getMerchantId())
                    .productSku(orderReq.getProductSku())
                    .quantity(orderReq.getQuantity())
                    .amount(checkProductResult.getTotalAmount())
                    .build();
        merchantFeignClient.productSold(productSoldReq);

        // 9. 保存訂單到數據庫
        Order order = Order.builder()
                .orderId(orderId)
                .userId(userId)
                .merchantId(checkProductResult.getMerchantId())
                .productSku(orderReq.getProductSku())
                .quantity(orderReq.getQuantity())
                .totalAmount(checkProductResult.getTotalAmount())
                .createdAt(new Date())
                .build();
        orderMapper.insert(order);
    }
}
