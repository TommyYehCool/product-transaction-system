package com.ap_capital.common.req.merchant_module.merchant;

import com.ap_capital.common.cnst.CommonStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateMerchantReq {
    private String name;
    private BigDecimal accountBalance;
    private CommonStatus status;
}
