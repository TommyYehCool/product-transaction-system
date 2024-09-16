package com.ap_capital.common.req.merchant_module.merchant;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductSoldReq {
    private Long orderId;
    private Long merchantId;
    private String productSku;
    private Long userId;
    private int quantity;
    private BigDecimal amount;
}
