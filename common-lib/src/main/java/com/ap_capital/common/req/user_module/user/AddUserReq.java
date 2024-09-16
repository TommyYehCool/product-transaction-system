package com.ap_capital.common.req.user_module.user;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AddUserReq {
    private String name;
    private BigDecimal prepaidAccountBalance;
}
