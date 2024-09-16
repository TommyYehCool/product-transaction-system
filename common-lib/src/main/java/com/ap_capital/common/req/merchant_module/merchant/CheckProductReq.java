package com.ap_capital.common.req.merchant_module.merchant;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckProductReq {
    private String productSku;
    private int quantity;
}
