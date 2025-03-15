package com.mgt.domain.repository;

import com.mgt.domain.entity.RolePermission;
import java.util.List;
import java.util.Optional;

public interface RolePermissionRepository {
    // 保存角色权限关联
    RolePermission save(RolePermission rolePermission);
    
    // 根据ID查询角色权限关联
    Optional<RolePermission> findById(String id);
    
    // 根据角色ID和权限ID查询角色权限关联
    Optional<RolePermission> findByRoleIdAndPermissionId(String roleId, String permissionId);
    
    // 根据角色ID查询角色权限关联列表
    List<RolePermission> findByRoleId(String roleId);
    
    // 根据权限ID查询角色权限关联列表
    List<RolePermission> findByPermissionId(String permissionId);
    
    // 删除角色权限关联
    void deleteById(String id);
    
    // 根据角色ID删除角色权限关联
    void deleteByRoleId(String roleId);
    
    // 根据权限ID删除角色权限关联
    void deleteByPermissionId(String permissionId);
    
    // 根据角色ID和权限ID删除角色权限关联
    void deleteByRoleIdAndPermissionId(String roleId, String permissionId);
    
    // 批量保存角色权限关联
    List<RolePermission> batchSave(List<RolePermission> rolePermissions);
} 