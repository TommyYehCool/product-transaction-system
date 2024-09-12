package com.ap_capital.common.req.user_module.order;

import lombok.Data;

@Data
public class CreateOrderReq {
    private String productSku;
    private int quantity;
}
