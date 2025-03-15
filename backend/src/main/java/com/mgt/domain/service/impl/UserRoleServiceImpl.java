package com.mgt.domain.service.impl;

import com.mgt.domain.entity.UserRole;
import com.mgt.domain.repository.UserRepository;
import com.mgt.domain.repository.UserRoleRepository;
import com.mgt.domain.service.UserRoleService;
import com.mgt.infrastructure.config.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void assignUserRoles(String userId, List<String> roleIds) {
        // 检查用户是否存在
        userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        // 删除原有角色
        userRoleRepository.deleteByUserId(userId);
        
        // 添加新角色
        if (roleIds != null && !roleIds.isEmpty()) {
            List<UserRole> userRoles = roleIds.stream()
                .map(roleId -> {
                    UserRole userRole = new UserRole();
                    userRole.setId(UUID.randomUUID().toString());
                    userRole.setUserId(userId);
                    userRole.setRoleId(roleId);
                    userRole.setCreateTime(LocalDateTime.now());
                    return userRole;
                })
                .collect(Collectors.toList());
            
            userRoleRepository.batchSave(userRoles);
        }
    }

    @Override
    public List<String> getUserRoles(String userId) {
        // 检查用户是否存在
        userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        return userRoleRepository.findByUserId(userId).stream()
            .map(UserRole::getRoleId)
            .collect(Collectors.toList());
    }

    @Override
    public List<String> getRoleUsers(String roleId) {
        return userRoleRepository.findByRoleId(roleId).stream()
            .map(UserRole::getUserId)
            .collect(Collectors.toList());
    }

    @Override
    public boolean hasRole(String userId, String roleId) {
        return userRoleRepository.findByUserIdAndRoleId(userId, roleId).isPresent();
    }

    @Override
    public List<UserRole> getUserRoleList(String userId) {
        // 检查用户是否存在
        userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException("用户不存在"));
        
        return userRoleRepository.findByUserId(userId);
    }
} 