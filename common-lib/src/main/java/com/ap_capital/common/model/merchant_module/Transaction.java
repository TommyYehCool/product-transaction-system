package com.ap_capital.common.model.merchant_module;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class Transaction {
    private Long transactionId;
    private Long orderId;
    private Long userId;
    private Long merchantId;
    private String productSku;
    private int quantity;
    private BigDecimal totalAmount;
    private Date transactionDate;
    private String transactionType;
    private Date createdAt;
    private Date updatedAt;
}
