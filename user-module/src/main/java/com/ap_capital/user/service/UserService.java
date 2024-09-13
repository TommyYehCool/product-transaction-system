package com.ap_capital.user.service;

import com.ap_capital.common.cnst.CommonStatus;
import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.user_module.user.AddUserReq;
import com.ap_capital.common.req.user_module.user.UpdateUserReq;
import com.ap_capital.user.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void addUser(AddUserReq req) {
        User user = new User();
        BeanUtils.copyProperties(req, user);
        user.setStatus(CommonStatus.Active.getValue());
        user.setCreatedAt(new Date());
        userMapper.insert(user);
    }

    public void updateUser(Long userId, UpdateUserReq req) {
        User user = new User();
        BeanUtils.copyProperties(req, user);
        user.setUserId(userId);
        if (req.getPrepaidAccountBalance() != null) {
            user.setPrepaidAccountBalance(req.getPrepaidAccountBalance());
        }
        user.setStatus(req.getStatus().getValue());
        userMapper.update(user);
    }

    public List<User> getAllUsers() {
        return userMapper.findAll();
    }

    public User findById(Long userId) {
        return userMapper.findById(userId);
    }

    public void recharge(Long userId, BigDecimal amount) {
        // 校驗充值金額
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero.");
        }
        userMapper.recharge(userId, amount, new Date());
    }
}
