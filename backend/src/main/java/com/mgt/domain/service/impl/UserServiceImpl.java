package com.mgt.domain.service.impl;

import com.mgt.domain.entity.User;
import com.mgt.domain.repository.UserRepository;
import com.mgt.domain.service.UserService;
import com.mgt.infrastructure.config.BusinessException;
import com.mgt.infrastructure.utils.JwtUtils;
import com.mgt.infrastructure.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User createUser(User user) {
        // 检查用户名是否存在
        if (isUsernameExists(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (user.getEmail() != null && isEmailExists(user.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }
        
        // 检查手机号是否存在
        if (user.getPhone() != null && isPhoneExists(user.getPhone())) {
            throw new BusinessException("手机号已存在");
        }
        
        // 设置基本信息
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus("1"); // 默认启用
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(String id, User user) {
        // 检查用户是否存在
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 检查用户名是否存在
        if (!existingUser.getUsername().equals(user.getUsername()) && isUsernameExists(user.getUsername())) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否存在
        if (user.getEmail() != null && !Objects.equals(existingUser.getEmail(), user.getEmail()) && isEmailExists(user.getEmail())) {
            throw new BusinessException("邮箱已存在");
        }
        
        // 检查手机号是否存在
        if (user.getPhone() != null && !Objects.equals(existingUser.getPhone(), user.getPhone()) && isPhoneExists(user.getPhone())) {
            throw new BusinessException("手机号已存在");
        }
        
        // 更新基本信息
        user.setId(id);
        user.setPassword(existingUser.getPassword()); // 不更新密码
        user.setUpdateTime(LocalDateTime.now());
        user.setCreateTime(existingUser.getCreateTime());
        user.setLastLoginTime(existingUser.getLastLoginTime());
        
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        // 检查用户是否存在
        userRepository.findById(id)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        userRepository.deleteById(id);
    }

    @Override
    public User getUser(String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new BusinessException("用户不存在"));
    }

    @Override
    public Map<String, Object> getUserList(User condition, int page, int size) {
        List<User> users = userRepository.findByCondition(condition, page, size);
        long total = userRepository.countByCondition(condition);
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", users);
        result.put("total", total);
        
        return result;
    }

    @Override
    @Transactional
    public void updatePassword(String id, String oldPassword, String newPassword) {
        // 检查用户是否存在
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException("旧密码不正确");
        }
        
        // 更新密码
        userRepository.updatePassword(id, passwordEncoder.encode(newPassword));
    }

    @Override
    @Transactional
    public String resetPassword(String id) {
        // 检查用户是否存在
        userRepository.findById(id)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 生成随机密码
        String newPassword = generateRandomPassword();
        
        // 更新密码
        userRepository.updatePassword(id, passwordEncoder.encode(newPassword));
        
        return newPassword;
    }

    @Override
    @Transactional
    public void updateStatus(String id, String status) {
        // 检查用户是否存在
        userRepository.findById(id)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        userRepository.updateStatus(id, status);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        // 根据用户名查询用户
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("用户名或密码不正确"));
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码不正确");
        }
        
        // 检查用户状态
        if (!"1".equals(user.getStatus())) {
            throw new BusinessException("用户已被禁用");
        }
        
        // 更新最后登录时间
        userRepository.updateLastLoginTime(user.getId());
        
        // 生成token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        
        // 清除敏感信息
        user.setPassword(null);
        result.put("user", user);
        
        return result;
    }

    @Override
    public void logout(String token) {
        // 可以在这里实现token黑名单等逻辑
    }

    @Override
    public User getCurrentUser() {
        // 这里需要从当前请求上下文中获取用户信息
        // 实际实现可能需要依赖于具体的框架
        return null;
    }

    @Override
    public boolean isUsernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }
    
    // 生成随机密码
    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
} 