package com.ap_capital.user.controller;

import com.ap_capital.common.model.user_module.User;
import com.ap_capital.common.req.user_module.AddUserReq;
import com.ap_capital.common.req.user_module.RechargeRequest;
import com.ap_capital.common.req.user_module.UpdateUserReq;
import com.ap_capital.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void addUser(@RequestBody AddUserReq req) {
        userService.addUser(req);
    }

    @PutMapping("/{userId}")
    public void updateUser(@PathVariable Long userId, @RequestBody UpdateUserReq req) {
        userService.updateUser(userId, req);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getByUserId(userId));
    }

    @PostMapping("/{userId}/recharge")
    public ResponseEntity<?> recharge(@PathVariable Long userId, @RequestBody RechargeRequest request) {
        try {
            userService.recharge(userId, request.getAmount());
            return ResponseEntity.ok("Recharge successful");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Recharge failed: " + e.getMessage());
        }
    }

}