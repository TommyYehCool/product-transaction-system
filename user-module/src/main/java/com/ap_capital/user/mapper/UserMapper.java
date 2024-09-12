package com.ap_capital.user.mapper;

import com.ap_capital.common.model.user_module.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("""
        SELECT * FROM users
    """)
    List<User> getAll();

    @Select("SELECT * FROM users WHERE user_id = #{userId}")
    User getByUserId(Long userId);

    @Insert("""
        INSERT INTO users 
            (name, prepaid_account_balance, status, created_at) 
        VALUES
            (#{name}, #{prepaidAccountBalance}, #{status}, #{createdAt})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insert(User user);

    @Update("""
        UPDATE 
            users 
        SET 
            name = #{name}, 
            prepaid_account_balance = #{prepaidAccountBalance},
            status = #{status},  
            updated_at = #{updatedAt} 
        WHERE 
            user_id = #{userId}
    """)
    void update(User user);

    @Update("""
        UPDATE 
            users
        SET
            prepaid_account_balance = prepaid_account_balance + #{amount}
        WHERE
            user_id = #{userId}
    """)
    void recharge(@Param("userId") Long userId, @Param("amount") BigDecimal amount);
}
