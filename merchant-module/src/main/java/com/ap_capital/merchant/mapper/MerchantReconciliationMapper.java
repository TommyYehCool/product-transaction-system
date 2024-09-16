package com.ap_capital.merchant.mapper;

import com.ap_capital.common.model.merchant_module.MerchantReconciliation;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MerchantReconciliationMapper {

    @Insert("""
        INSERT INTO merchant_reconciliation 
            (reconciliation_id, merchant_id, reconciliation_date, total_revenue, sold_quantity, created_at, updated_at) 
        VALUES 
            (#{reconciliationId}, #{merchantId}, #{reconciliationDate}, #{totalRevenue}, #{soldQuantity}, #{createdAt}, #{updatedAt})
    """)
    void insertReconciliation(MerchantReconciliation reconciliation);
}