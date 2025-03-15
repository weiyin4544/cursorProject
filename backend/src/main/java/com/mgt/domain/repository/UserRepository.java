package com.mgt.domain.repository;

import com.mgt.domain.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    // 保存用户
    User save(User user);
    
    // 根据ID查询用户
    Optional<User> findById(String id);
    
    // 根据用户名查询用户
    Optional<User> findByUsername(String username);
    
    // 根据邮箱查询用户
    Optional<User> findByEmail(String email);
    
    // 根据手机号查询用户
    Optional<User> findByPhone(String phone);
    
    // 查询用户列表
    List<User> findAll();
    
    // 根据条件查询用户列表
    List<User> findByCondition(User condition, int page, int size);
    
    // 统计符合条件的用户数量
    long countByCondition(User condition);
    
    // 删除用户
    void deleteById(String id);
    
    // 更新用户密码
    void updatePassword(String id, String newPassword);
    
    // 更新用户状态
    void updateStatus(String id, String status);
    
    // 更新用户最后登录时间
    void updateLastLoginTime(String id);
    
    // 检查用户名是否存在
    boolean existsByUsername(String username);
    
    // 检查邮箱是否存在
    boolean existsByEmail(String email);
    
    // 检查手机号是否存在
    boolean existsByPhone(String phone);
} 