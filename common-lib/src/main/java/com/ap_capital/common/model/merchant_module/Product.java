package com.ap_capital.common.model.merchant_module;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Product {
    private String productSku;
    private String name;
    private BigDecimal price;
    private int availableQuantity;
    private Long merchantId;
    private Date createdAt;
    private Date updatedAt;
}
