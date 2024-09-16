package com.ap_capital.user.test;

import com.ap_capital.common.cnst.CommonStatus;
import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.user_module.order.CreateOrderReq;
import com.ap_capital.common.req.user_module.user.AddUserReq;
import com.ap_capital.common.req.user_module.user.RechargeRequest;
import com.ap_capital.common.req.user_module.user.UpdateUserReq;
import com.ap_capital.user.controller.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testAddUser() throws Exception {
        AddUserReq req = AddUserReq.builder()
                .name("Tommy Yeh")
                .prepaidAccountBalance(BigDecimal.valueOf(50.00))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Tommy Yeh\",\"prepaidAccountBalance\":50.00}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Add user succeed"))
                .andDo(print());

        verify(userService).addUser(any(AddUserReq.class));
    }

    @Test
    public void testUpdateUser() throws Exception {
        UpdateUserReq req = UpdateUserReq.builder()
                .prepaidAccountBalance(BigDecimal.valueOf(100.00))
                .status(CommonStatus.Inactive)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"prepaidAccountBalance\":100.00,\"status\":\"Inactive\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Update user succeed"))
                .andDo(print());

        verify(userService).updateUser(eq(1L), any(UpdateUserReq.class));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        User user2 = new User();
        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(2))
                .andDo(print());

        verify(userService).getAllUsers();
    }

    @Test
    public void testFindById() throws Exception {
        User user = new User();
        user.setUserId(1L);
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(1))
                .andDo(print());

        verify(userService).findById(1L);
    }

    @Test
    public void testRecharge() throws Exception {
        RechargeRequest req = new RechargeRequest();
        req.setAmount(BigDecimal.valueOf(50.00));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":50.00}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Recharge successful"))
                .andDo(print());

        verify(userService).recharge(eq(1L), eq(BigDecimal.valueOf(50.00)));
    }

    @Test
    public void testRecharge_Failure() throws Exception {
        RechargeRequest req = new RechargeRequest();
        req.setAmount(BigDecimal.valueOf(0.00));
        doThrow(new IllegalArgumentException("Amount must be greater than zero.")).when(userService).recharge(eq(1L), eq(BigDecimal.valueOf(0.00)));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/recharge")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":0.00}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Recharge failed: Amount must be greater than zero."))
                .andDo(print());

        verify(userService).recharge(eq(1L), eq(BigDecimal.valueOf(0.00)));
    }

    @Test
    public void testOrder() throws Exception {
        CreateOrderReq req = new CreateOrderReq();
        // Set required fields for CreateOrderReq here
        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Order created successfully"))
                .andDo(print());

        verify(orderService).createOrder(eq(1L), any(CreateOrderReq.class));
    }

    @Test
    public void testOrder_Failure() throws Exception {
        CreateOrderReq req = new CreateOrderReq();
        // Set required fields for CreateOrderReq here
        doThrow(new RuntimeException("Order creation failed")).when(orderService).createOrder(eq(1L), any(CreateOrderReq.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Order creation failed"))
                .andDo(print());

        verify(orderService).createOrder(eq(1L), any(CreateOrderReq.class));
    }
}
