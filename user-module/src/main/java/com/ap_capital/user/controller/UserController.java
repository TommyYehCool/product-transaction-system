package com.ap_capital.user.controller;

import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.user_module.order.CreateOrderReq;
import com.ap_capital.common.req.user_module.user.AddUserReq;
import com.ap_capital.common.req.user_module.user.RechargeRequest;
import com.ap_capital.common.req.user_module.user.UpdateUserReq;
import com.ap_capital.user.service.OrderService;
import com.ap_capital.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final OrderService orderService;

    public UserController(UserService userService, OrderService orderService) {
        this.userService = userService;
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody AddUserReq req) {
        userService.addUser(req);
        return ResponseEntity.ok("Add user succeed");
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable Long userId, @RequestBody UpdateUserReq req) {
        userService.updateUser(userId, req);
        return ResponseEntity.ok("Update user succeed");
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findById(userId));
    }

    @PostMapping("/{userId}/recharge")
    public ResponseEntity<?> recharge(@PathVariable Long userId, @RequestBody RechargeRequest req) {
        try {
            userService.recharge(userId, req.getAmount());
            return ResponseEntity.ok("Recharge successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Recharge failed: " + e.getMessage());
        }
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<?> order(@PathVariable Long userId, @RequestBody CreateOrderReq req) {
        try {
            orderService.createOrder(userId, req);
            return ResponseEntity.ok("Order created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}