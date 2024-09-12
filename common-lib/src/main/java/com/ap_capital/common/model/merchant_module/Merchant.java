package com.ap_capital.common.model.merchant_module;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Merchant {
    private Long merchantId;
    private String name;
    private BigDecimal accountBalance;
    private Integer status;
    private Date createdAt;
    private Date updatedAt;
}
