package com.mgt.domain.service;

import com.mgt.domain.entity.Permission;
import java.util.List;

public interface PermissionService {
    // 创建权限
    Permission createPermission(Permission permission);
    
    // 更新权限
    Permission updatePermission(String id, Permission permission);
    
    // 删除权限
    void deletePermission(String id);
    
    // 获取权限详情
    Permission getPermission(String id);
    
    // 获取权限列表
    List<Permission> getPermissionList(Permission condition);
    
    // 更新权限状态
    void updateStatus(String id, String status);
    
    // 检查权限编码是否存在
    boolean isCodeExists(String code);
    
    // 获取权限树
    List<Permission> getPermissionTree();
    
    // 根据角色ID获取权限列表
    List<Permission> getPermissionsByRoleId(String roleId);
    
    // 根据用户ID获取权限列表
    List<Permission> getPermissionsByUserId(String userId);
} 