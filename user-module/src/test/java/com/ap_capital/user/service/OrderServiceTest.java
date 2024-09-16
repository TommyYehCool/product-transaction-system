package com.ap_capital.user.service;

import com.ap_capital.common.feign_client.merchant_module.MerchantFeignClient;
import com.ap_capital.common.model.user_module.Order;
import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.merchant_module.merchant.CheckProductReq;
import com.ap_capital.common.req.merchant_module.merchant.ProductSoldReq;
import com.ap_capital.common.req.user_module.order.CreateOrderReq;
import com.ap_capital.common.resp.merchant_module.merchat.CheckProductResp;
import com.ap_capital.user.mapper.OrderMapper;
import com.ap_capital.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private OrderMapper orderMapper;

    @MockBean
    private MerchantFeignClient merchantFeignClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Transactional
    public void testCreateOrder() {
        Long userId = 1L;
        CreateOrderReq orderReq
                = CreateOrderReq.builder()
                    .productSku("sku123")
                    .quantity(2)
                    .build();

        User user = new User();
        user.setUserId(userId);
        user.setPrepaidAccountBalance(BigDecimal.valueOf(100.00));

        CheckProductResp checkProductResp
                = CheckProductResp.builder()
                    .merchantId(1L)
                    .totalAmount(BigDecimal.valueOf(50.00))
                    .build();

        // 使用 mock 的時候需要保證使用 mock 對象
        when(userMapper.findById(userId)).thenReturn(user);
        when(merchantFeignClient.checkProduct(any(CheckProductReq.class))).thenReturn(ResponseEntity.ok(checkProductResp));

        orderService.createOrder(userId, orderReq);

        // 驗證方法調用
        verify(userMapper).findById(userId);
        verify(merchantFeignClient).checkProduct(any(CheckProductReq.class));
        verify(userMapper).paid(eq(userId), any(BigDecimal.class), any(Date.class));
        verify(merchantFeignClient).productSold(any(ProductSoldReq.class));
        verify(orderMapper).insert(any(Order.class));
    }

    @Test
    public void testCreateOrder_InsufficientBalance() {
        Long userId = 1L;
        CreateOrderReq orderReq
                = CreateOrderReq.builder()
                    .productSku("sku123")
                    .quantity(2)
                    .build();

        User user = new User();
        user.setUserId(userId);
        user.setPrepaidAccountBalance(BigDecimal.valueOf(30.00)); // Less than required

        CheckProductResp checkProductResp
                = CheckProductResp.builder()
                    .merchantId(1L)
                    .totalAmount(BigDecimal.valueOf(50.00))
                    .build();

        when(userMapper.findById(userId)).thenReturn(user);
        when(merchantFeignClient.checkProduct(any(CheckProductReq.class))).thenReturn(ResponseEntity.ok(checkProductResp));

        // 期望異常
        RuntimeException thrown = null;
        try {
            orderService.createOrder(userId, orderReq);
        } catch (RuntimeException e) {
            thrown = e;
        }

        assertNotNull(thrown);
        assertEquals("Insufficient balance in user's prepaid account.", thrown.getMessage());
    }
}