package com.mgt.domain.service;

import com.mgt.domain.entity.User;
import java.util.List;
import java.util.Map;

public interface UserService {
    // 创建用户
    User createUser(User user);
    
    // 更新用户
    User updateUser(String id, User user);
    
    // 删除用户
    void deleteUser(String id);
    
    // 获取用户详情
    User getUser(String id);
    
    // 获取用户列表
    Map<String, Object> getUserList(User condition, int page, int size);
    
    // 更新用户密码
    void updatePassword(String id, String oldPassword, String newPassword);
    
    // 重置用户密码
    String resetPassword(String id);
    
    // 更新用户状态
    void updateStatus(String id, String status);
    
    // 用户登录
    Map<String, Object> login(String username, String password);
    
    // 用户登出
    void logout(String token);
    
    // 获取当前用户信息
    User getCurrentUser();
    
    // 检查用户名是否存在
    boolean isUsernameExists(String username);
    
    // 检查邮箱是否存在
    boolean isEmailExists(String email);
    
    // 检查手机号是否存在
    boolean isPhoneExists(String phone);
} 