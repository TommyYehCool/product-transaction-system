package com.ap_capital.common.req.merchant_module.merchant;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddMerchantReq {
    private String name;
    private BigDecimal accountBalance;
}
