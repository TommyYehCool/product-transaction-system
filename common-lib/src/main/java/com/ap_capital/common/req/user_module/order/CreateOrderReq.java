package com.ap_capital.common.req.user_module.order;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateOrderReq {
    private String productSku;
    private int quantity;
}
