package com.ap_capital.common.req.user_module.user;

import com.ap_capital.common.cnst.CommonStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UpdateUserReq {
    private String name;
    private BigDecimal prepaidAccountBalance;
    private CommonStatus status;
}
