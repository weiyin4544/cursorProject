package com.mgt.domain.service;

import com.mgt.domain.entity.UserRole;
import java.util.List;

public interface UserRoleService {
    // 分配用户角色
    void assignUserRoles(String userId, List<String> roleIds);
    
    // 获取用户角色
    List<String> getUserRoles(String userId);
    
    // 获取角色用户
    List<String> getRoleUsers(String roleId);
    
    // 检查用户是否有角色
    boolean hasRole(String userId, String roleId);
    
    // 获取用户角色关联列表
    List<UserRole> getUserRoleList(String userId);
} 