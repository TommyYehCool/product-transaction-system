package com.ap_capital.common.req.user_module.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderReq {
    private String productSku;
    private int quantity;
}
