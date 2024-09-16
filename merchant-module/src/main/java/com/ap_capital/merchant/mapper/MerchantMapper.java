package com.ap_capital.merchant.mapper;

import com.ap_capital.common.model.merchant_module.Merchant;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface MerchantMapper {

    @Select("""
        SELECT * FROM merchants
    """)
    @Results(
            id = "merchantResultMap",
            value = {
                    @Result(column = "merchant_id", property = "merchantId"),
                    @Result(column = "name", property = "name"),
                    @Result(column = "account_balance", property = "accountBalance"),
                    @Result(column = "status", property = "status"),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "updated_at", property = "updatedAt")
            }
    )
    List<Merchant> findAll();

    @Select("""
        SELECT * FROM merchants WHERE `status` = 1
    """)
    @ResultMap("merchantResultMap")
    List<Merchant> findAllActivateMerchants();

    @Select("""
        SELECT * FROM merchants WHERE merchant_id = #{merchantId}
    """)
    @ResultMap("merchantResultMap")
    Merchant findById(@Param("merchantId") Long merchantId);

    @Select("""
        SELECT * FROM merchants WHERE name = #{name}
    """)
    @ResultMap("merchantResultMap")
    Merchant findByName(String name);

    @Insert("""
        INSERT INTO merchants 
            (name, account_balance, status, created_at) 
        VALUES 
            (#{name}, #{accountBalance}, #{status}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "merchantId")
    void insert(Merchant merchant);

    @Update("""
        UPDATE 
            merchants 
        SET 
            name = #{name}, 
            account_balance = #{accountBalance},
            status = #{status}, 
            updated_at = #{updatedAt} 
        WHERE 
            merchant_id = #{merchantId}
    """)
    void update(Merchant merchant);

    @Update("""
        UPDATE 
            merchants 
        SET 
            account_balance = account_balance + #{amount},
            updated_at = #{updatedAt} 
        WHERE 
            merchant_id = #{merchantId}
    """)
    void increaseMerchantAccountBalance(
            @Param("merchantId") Long merchantId,
            @Param("amount") BigDecimal amount,
            @Param("updatedAt") Date updatedAt
    );
}
