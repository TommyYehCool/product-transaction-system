package com.ap_capital.common.model.user_module;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Order {
    private String orderId;
    private Long userId;
    private Long merchantId;
    private String productSku;
    private int quantity;
    private BigDecimal totalAmount;
    private Date createdAt;
}
