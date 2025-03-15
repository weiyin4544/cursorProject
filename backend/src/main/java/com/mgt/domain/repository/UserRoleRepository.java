package com.mgt.domain.repository;

import com.mgt.domain.entity.UserRole;
import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    // 保存用户角色关联
    UserRole save(UserRole userRole);
    
    // 根据ID查询用户角色关联
    Optional<UserRole> findById(String id);
    
    // 根据用户ID和角色ID查询用户角色关联
    Optional<UserRole> findByUserIdAndRoleId(String userId, String roleId);
    
    // 根据用户ID查询用户角色关联列表
    List<UserRole> findByUserId(String userId);
    
    // 根据角色ID查询用户角色关联列表
    List<UserRole> findByRoleId(String roleId);
    
    // 删除用户角色关联
    void deleteById(String id);
    
    // 根据用户ID删除用户角色关联
    void deleteByUserId(String userId);
    
    // 根据角色ID删除用户角色关联
    void deleteByRoleId(String roleId);
    
    // 根据用户ID和角色ID删除用户角色关联
    void deleteByUserIdAndRoleId(String userId, String roleId);
    
    // 批量保存用户角色关联
    List<UserRole> batchSave(List<UserRole> userRoles);
} 