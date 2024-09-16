package com.ap_capital.merchant.mapper;

import com.ap_capital.common.model.merchant_module.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface TransactionMapper {

    @Insert("""
        INSERT INTO transactions 
            (transaction_id, order_id, user_id, merchant_id, product_sku, quantity, total_amount, transaction_date, transaction_type, created_at) 
        VALUES 
            (#{transactionId}, #{orderId}, #{userId}, #{merchantId}, #{productSku}, #{quantity}, #{totalAmount}, #{transactionDate}, #{transactionType}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "transactionId")
    void insertTransaction(Transaction transaction);

    @Select("""
        SELECT * FROM transactions WHERE transaction_id = #{transactionId}
    """)
    @Results(
            id = "transactionResultMap",
            value = {
                    @Result(property = "transactionId", column = "transaction_id"),
                    @Result(property = "orderId", column = "order_id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "merchantId", column = "merchant_id"),
                    @Result(property = "productSku", column = "product_sku"),
                    @Result(property = "totalAmount", column = "total_amount"),
                    @Result(property = "transactionDate", column = "transaction_date"),
                    @Result(property = "transactionType", column = "transaction_type"),
                    @Result(property = "createdAt", column = "created_at"),
                    @Result(property = "updatedAt", column = "updated_at")
            }
    )
    Transaction findTransactionById(Long transactionId);

    @Select("""
        SELECT * FROM transactions WHERE merchant_id = #{merchantId}
    """)
    @ResultMap("transactionResultMap")
    List<Transaction> findTransactionsByMerchantId(Long merchantId);

    @Select("""
        SELECT * FROM transactions WHERE order_id = #{orderId}
    """)
    @ResultMap("transactionResultMap")
    Transaction findTransactionByOrderId(String orderId);

    @Select("""
        SELECT * FROM transactions WHERE merchant_id = #{merchantId} AND transaction_date >= #{startDate} AND transaction_date <= #{endDate}
    """)
    @ResultMap("transactionResultMap")
    List<Transaction> findTransactionsByMerchantIdAndDateRange(
            @Param("merchantId") Long merchantId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

}
