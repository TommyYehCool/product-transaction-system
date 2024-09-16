package com.ap_capital.user.test;

import com.ap_capital.common.cnst.CommonStatus;
import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.user_module.user.AddUserReq;
import com.ap_capital.common.req.user_module.user.UpdateUserReq;
import com.ap_capital.user.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() {
        AddUserReq addUserReq
                = AddUserReq.builder()
                    .name("Tommy Yeh")
                    .prepaidAccountBalance(BigDecimal.valueOf(50.00))
                    .build();

        userService.addUser(addUserReq);

        verify(userMapper).insert(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals("Tommy Yeh", capturedUser.getName());
        assertEquals(BigDecimal.valueOf(50.00), capturedUser.getPrepaidAccountBalance());
        assertEquals(CommonStatus.Active.getValue(), capturedUser.getStatus());
        assertNotNull(capturedUser.getCreatedAt());
        assertNull(capturedUser.getUpdatedAt());
    }

    @Test
    public void testUpdateUser() {
        Long userId = 1L;
        UpdateUserReq updateUserReq
                = UpdateUserReq.builder()
                    .prepaidAccountBalance(BigDecimal.valueOf(100.00))
                    .status(CommonStatus.Inactive)
                    .build();

        userService.updateUser(userId, updateUserReq);

        User user = new User();
        BeanUtils.copyProperties(updateUserReq, user);
        user.setUserId(userId);

        verify(userMapper).update(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        assertEquals(userId, capturedUser.getUserId());
        assertEquals(BigDecimal.valueOf(100.00), capturedUser.getPrepaidAccountBalance());
        assertEquals(CommonStatus.Inactive.getValue(), capturedUser.getStatus());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());

        when(userMapper.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        Long userId = 1L;
        User user = new User();
        user.setUserId(userId);

        when(userMapper.findById(userId)).thenReturn(user);

        User result = userService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
    }

    @Test
    public void testRecharge() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(50.00);

        userService.recharge(userId, amount);

        verify(userMapper).recharge(eq(userId), eq(amount), any(Date.class));
    }

    @Test
    public void testRecharge_AmountLessThanOrEqualToZero() {
        Long userId = 1L;
        BigDecimal amount = BigDecimal.valueOf(0.00);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            userService.recharge(userId, amount);
        });

        assertEquals("Amount must be greater than zero.", thrown.getMessage());
    }
}
