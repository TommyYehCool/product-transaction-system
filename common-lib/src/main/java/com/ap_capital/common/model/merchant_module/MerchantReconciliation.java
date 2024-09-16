package com.ap_capital.common.model.merchant_module;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class MerchantReconciliation {
    private Long reconciliationId;
    private Long merchantId;
    private Date reconciliationDate;
    private BigDecimal totalRevenue;
    private int soldQuantity;
    private Date createdAt;
    private Date updatedAt;
}
