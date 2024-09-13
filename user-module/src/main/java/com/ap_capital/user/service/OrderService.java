package com.ap_capital.user.service;

import com.ap_capital.common.feign_client.merchant_module.MerchantFeignClient;
import com.ap_capital.common.feign_client.merchant_module.ProductFeignClient;
import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.common.model.user_module.Order;
import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.user_module.order.CreateOrderReq;
import com.ap_capital.common.utils.UUIDUtils;
import com.ap_capital.user.mapper.OrderMapper;
import com.ap_capital.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class OrderService {

    private final UserMapper userMapper;
    private final ProductFeignClient productFeignClient;
    private final MerchantFeignClient merchantFeignClient;
    private final OrderMapper orderMapper;

    public OrderService(UserMapper userMapper, ProductFeignClient productFeignClient, MerchantFeignClient merchantFeignClient, OrderMapper orderMapper) {
        this.userMapper = userMapper;
        this.productFeignClient = productFeignClient;
        this.merchantFeignClient = merchantFeignClient;
        this.orderMapper = orderMapper;
    }

    @Transactional
    public void createOrder(Long userId, CreateOrderReq orderReq) {
        // 1. 查詢用戶
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("User not found with ID: " + userId);
        }

        // 2. 查詢產品庫存
        Product product = productFeignClient.getProductBySku(orderReq.getProductSku());
        if (product == null) {
            throw new RuntimeException("Product not found with SKU: " + orderReq.getProductSku());
        }

        // 3. 檢查庫存是否足夠
        if (product.getAvailableQuantity() < orderReq.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getName());
        }

        // 4. 計算訂單總金額
        BigDecimal orderAmount = product.getPrice().multiply(BigDecimal.valueOf(orderReq.getQuantity()));

        // 5. 檢查用戶的預付帳戶餘額是否足夠
        if (user.getPrepaidAccountBalance().compareTo(orderAmount) < 0) {
            throw new RuntimeException("Insufficient balance in user's prepaid account.");
        }

        final String orderId = UUIDUtils.getUUID();

        // 6. 從用戶預付帳戶中扣除相應金額
        userMapper.paid(userId, orderAmount, new Date());

        // 7. 從庫存中減少產品數量
        productFeignClient.productSold(product.getProductSku(), orderReq.getQuantity());

        // 8. 增加商家的賬戶餘額
        // 這裡假設我們可以根據 product 的 merchantId 查詢商家並更新賬戶餘額
        Long merchantId = product.getMerchantId();
        BigDecimal revenue = product.getPrice().multiply(BigDecimal.valueOf(orderReq.getQuantity()));
        merchantFeignClient.incrementMerchantRevenue(merchantId, revenue);

        // 9. 保存訂單到數據庫（這裡假設有一個訂單表和對應的 mapper
        Order order = Order.builder()
                .orderId(orderId)
                .userId(userId)
                .merchantId(merchantId)
                .productSku(product.getProductSku())
                .quantity(orderReq.getQuantity())
                .totalAmount(orderAmount)
                .createdAt(new Date())
                .build();
        orderMapper.insert(order);
    }
}
