package com.mgt.domain.service;

import com.mgt.domain.entity.Role;
import java.util.List;
import java.util.Map;

public interface RoleService {
    // 创建角色
    Role createRole(Role role);
    
    // 更新角色
    Role updateRole(String id, Role role);
    
    // 删除角色
    void deleteRole(String id);
    
    // 获取角色详情
    Role getRole(String id);
    
    // 获取角色列表
    Map<String, Object> getRoleList(Role condition, int page, int size);
    
    // 更新角色状态
    void updateStatus(String id, String status);
    
    // 分配权限
    void assignPermissions(String roleId, List<String> permissionIds);
    
    // 获取角色权限
    List<String> getRolePermissions(String roleId);
    
    // 检查角色编码是否存在
    boolean isCodeExists(String code);
    
    // 获取所有角色
    List<Role> getAllRoles();
    
    // 根据用户ID获取角色列表
    List<Role> getRolesByUserId(String userId);
} 