package com.mgt.interfaces.controller;

import com.mgt.domain.entity.User;
import com.mgt.domain.service.UserService;
import com.mgt.infrastructure.config.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public Result<User> createUser(@RequestBody User user) {
        return Result.success(userService.createUser(user));
    }

    @PutMapping("/{id}")
    public Result<User> updateUser(@PathVariable String id, @RequestBody User user) {
        return Result.success(userService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<User> getUser(@PathVariable String id) {
        return Result.success(userService.getUser(id));
    }

    @GetMapping
    public Result<Map<String, Object>> getUserList(User condition,
                                                  @RequestParam(defaultValue = "1") int page,
                                                  @RequestParam(defaultValue = "10") int size) {
        return Result.success(userService.getUserList(condition, page, size));
    }

    @PutMapping("/{id}/password")
    public Result<Void> updatePassword(@PathVariable String id,
                                      @RequestParam String oldPassword,
                                      @RequestParam String newPassword) {
        userService.updatePassword(id, oldPassword, newPassword);
        return Result.success();
    }

    @PutMapping("/{id}/reset-password")
    public Result<String> resetPassword(@PathVariable String id) {
        return Result.success(userService.resetPassword(id));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable String id, @RequestParam String status) {
        userService.updateStatus(id, status);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestParam String username, @RequestParam String password) {
        return Result.success(userService.login(username, password));
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader("Authorization") String token) {
        userService.logout(token.replace("Bearer ", ""));
        return Result.success();
    }

    @GetMapping("/current")
    public Result<User> getCurrentUser() {
        return Result.success(userService.getCurrentUser());
    }

    @GetMapping("/check-username")
    public Result<Boolean> checkUsername(@RequestParam String username) {
        return Result.success(userService.isUsernameExists(username));
    }

    @GetMapping("/check-email")
    public Result<Boolean> checkEmail(@RequestParam String email) {
        return Result.success(userService.isEmailExists(email));
    }

    @GetMapping("/check-phone")
    public Result<Boolean> checkPhone(@RequestParam String phone) {
        return Result.success(userService.isPhoneExists(phone));
    }
} 