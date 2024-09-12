package com.ap_capital.user.service;

import com.ap_capital.common.feign_client.merchant_module.ProductFeignClient;
import com.ap_capital.common.model.merchant_module.Product;
import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.user_module.order.CreateOrderReq;
import com.ap_capital.user.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final UserMapper userMapper;
    private final ProductFeignClient productFeignClient;

    public OrderService(UserMapper userMapper, ProductFeignClient productFeignClient) {
        this.userMapper = userMapper;
        this.productFeignClient = productFeignClient;
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
        BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(orderReq.getQuantity()));

        // 5. 檢查用戶的預付帳戶餘額是否足夠
        if (user.getPrepaidAccountBalance().compareTo(totalAmount) < 0) {
            throw new RuntimeException("Insufficient balance in user's prepaid account.");
        }

        // 6. 從用戶預付帳戶中扣除相應金額
        user.setPrepaidAccountBalance(user.getPrepaidAccountBalance().subtract(totalAmount));
        userMapper.update(user);

        // 7. 從庫存中減少產品數量
//        product.setAvailableQuantity(product.getAvailableQuantity() - orderReq.getQuantity());
//        productMapper.update(product);

        // 8. 增加商家的賬戶餘額
        // 這裡假設我們可以根據 product 的 merchantId 查詢商家並更新賬戶餘額
//        Long merchantId = product.getMerchantId();
//        BigDecimal merchantNewBalance = product.getPrice().multiply(BigDecimal.valueOf(orderReq.getQuantity()));
//        productMapper.updateMerchantBalance(merchantId, merchantNewBalance);

        // 9. 保存訂單到數據庫（這裡假設有一個訂單表和對應的 mapper）
        // orderMapper.insertOrder(userId, productSku, quantity, totalAmount);
    }
}
