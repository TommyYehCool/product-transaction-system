package com.ap_capital.user.mapper;

import com.ap_capital.common.model.user_module.User;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM users WHERE user_id = #{id}")
    User getByUserId(Long id);

    @Select("""
        SELECT * FROM users
    """)
    List<User> getAll();

    @Insert("""
        INSERT INTO users 
            (name, prepaid_account_balance, created_at) 
        VALUES
            (#{name}, #{prepaidAccountBalance}, #{createdAt})
        """)
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void insert(User user);

    @Update("""
        UPDATE 
            users 
        SET 
            name = #{name}, 
            prepaid_account_balance = #{prepaidAccountBalance}, 
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
