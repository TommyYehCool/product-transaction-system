package com.ap_capital.common.resp.merchant_module.merchat;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CheckProductResp {

    private boolean result;
    private String message;
    private Long merchantId;
    private BigDecimal totalAmount;

}
