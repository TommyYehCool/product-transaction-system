package com.ap_capital.user.mapper;

import com.ap_capital.common.model.user_module.Order;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderMapper {

    @Select("""
        SELECT * FROM orders WHERE order_id = #{orderId}
    """)
    Order findById(@Param("orderId") String orderId);

    @Select("""
        SELECT * FROM orders WHERE user_id = #{userId}
    """)
    List<Order> findByUserId(@Param("userId") Long userId);

    @Insert("""
        INSERT INTO orders 
            (
                order_id, user_id, merchant_id, product_sku, quantity, 
                total_amount, order_status, created_at, updated_at
            ) 
        VALUES 
            (
                #{orderId}, #{userId}, #{merchantId}, #{productSku}, #{quantity}, 
                #{totalAmount}, #{orderStatus}, #{createdAt}, #{updatedAt}
            )
    """)
    void insert(Order order);

    @Update("""
        UPDATE 
            orders 
        SET 
            order_status = #{orderStatus}, updated_at = #{updatedAt} 
        WHERE 
            order_id = #{orderId}
    """)
    void updateStatus(@Param("orderId") String orderId, @Param("orderStatus") String orderStatus, @Param("updatedAt") Date updatedAt);
}