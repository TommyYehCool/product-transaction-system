package com.ap_capital.merchant.mapper;

import com.ap_capital.common.model.merchant_module.Merchant;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MerchantMapper {

    @Select("""
        SELECT * FROM merchants WHERE merchant_id = #{merchantId}
    """)
    Merchant findById(@Param("merchantId") Long merchantId);

    @Select("""
        SELECT * FROM merchants
    """)
    List<Merchant> findAll();

    @Insert("""
        INSERT INTO merchants 
            (name, account_balance, created_at) 
        VALUES 
            (#{name}, #{accountBalance}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "merchantId")
    void insert(Merchant merchant);

    @Update("""
        UPDATE 
            merchants 
        SET 
            name = #{name}, account_balance = #{accountBalance}, updated_at = #{updatedAt} 
        WHERE 
            merchant_id = #{merchantId}
    """)
    void update(Merchant merchant);
}
