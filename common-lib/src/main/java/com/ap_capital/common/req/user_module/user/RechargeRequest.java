package com.ap_capital.common.req.user_module.user;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeRequest {
    private BigDecimal amount;
}
