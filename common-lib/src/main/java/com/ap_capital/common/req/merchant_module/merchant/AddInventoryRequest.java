package com.ap_capital.common.req.merchant_module.merchant;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddInventoryRequest {
    private String productSku;
    private String productName;
    private BigDecimal price;
    private int quantity;
}