package com.ap_capital.common.req.user_module.user;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddUserReq {
    private String name;
    private BigDecimal prepaidAccountBalance;
}
