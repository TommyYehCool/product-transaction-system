package com.ap_capital.merchant.mapper;

import com.ap_capital.common.model.merchant_module.Product;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProductMapper {

    @Select("""
        SELECT * FROM products
    """)
    @Results(
            id = "productResultMap",
            value = {
                    @Result(column = "product_sku", property = "productSku"),
                    @Result(column = "name", property = "name"),
                    @Result(column = "price", property = "price"),
                    @Result(column = "available_quantity", property = "availableQuantity"),
                    @Result(column = "merchant_id", property = "merchantId"),
                    @Result(column = "created_at", property = "createdAt"),
                    @Result(column = "updated_at", property = "updatedAt")
            }
    )
    List<Product> findAll();

    @Select("""
        SELECT * FROM products WHERE product_sku = #{productSku}
    """)
    @ResultMap("productResultMap")
    Product findBySku(@Param("productSku") String productSku);

    @Select("""
        SELECT * FROM products WHERE merchant_id = #{merchantId} AND name = #{name}
    """)
    @ResultMap("productResultMap")
    Product findByMerchantIdAndName(@Param("merchantId") Long merchantId, @Param("name") String name);

    @Insert("""
        INSERT INTO products 
            (product_sku, name, price, available_quantity, merchant_id, created_at) 
        VALUES 
            (#{productSku}, #{name}, #{price}, #{availableQuantity}, #{merchantId}, #{createdAt})
    """)
    void insert(Product product);

    @Update("""
        UPDATE 
            products 
        SET 
            name = #{name}, price = #{price}, available_quantity = #{availableQuantity}, updated_at = #{updatedAt} 
        WHERE 
            product_sku = #{productSku}
    """)
    void update(Product product);

    @Update("""
        UPDATE 
            products 
        SET
            available_quantity = available_quantity - #{quantity}, updated_at = #{updatedAt} 
        WHERE 
            product_sku = #{productSku}
    """)
    void productSold(String productSku, int quantity, Date updatedAt);
}
