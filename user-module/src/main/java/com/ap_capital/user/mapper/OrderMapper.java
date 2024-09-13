package com.ap_capital.user.mapper;

import com.ap_capital.common.model.user_module.Order;
import org.apache.ibatis.annotations.*;

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
                total_amount, created_at
            ) 
        VALUES 
            (
                #{orderId}, #{userId}, #{merchantId}, #{productSku}, #{quantity}, 
                #{totalAmount}, #{createdAt}
            )
    """)
    void insert(Order order);

}