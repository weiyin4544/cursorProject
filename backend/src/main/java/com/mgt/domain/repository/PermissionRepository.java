package com.mgt.domain.repository;

import com.mgt.domain.entity.Permission;
import java.util.List;
import java.util.Optional;

public interface PermissionRepository {
    // 保存权限
    Permission save(Permission permission);
    
    // 根据ID查询权限
    Optional<Permission> findById(String id);
    
    // 根据编码查询权限
    Optional<Permission> findByCode(String code);
    
    // 查询权限列表
    List<Permission> findAll();
    
    // 根据条件查询权限列表
    List<Permission> findByCondition(Permission condition);
    
    // 删除权限
    void deleteById(String id);
    
    // 更新权限状态
    void updateStatus(String id, String status);
    
    // 检查权限编码是否存在
    boolean existsByCode(String code);
    
    // 检查是否存在子权限
    boolean hasChildren(String id);
    
    // 根据角色ID查询权限列表
    List<Permission> findByRoleId(String roleId);
    
    // 根据用户ID查询权限列表
    List<Permission> findByUserId(String userId);
    
    // 查询权限树
    List<Permission> findTree();
} 