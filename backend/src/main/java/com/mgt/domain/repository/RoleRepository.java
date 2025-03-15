package com.mgt.domain.repository;

import com.mgt.domain.entity.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    // 保存角色
    Role save(Role role);
    
    // 根据ID查询角色
    Optional<Role> findById(String id);
    
    // 根据编码查询角色
    Optional<Role> findByCode(String code);
    
    // 查询角色列表
    List<Role> findAll();
    
    // 根据条件查询角色列表
    List<Role> findByCondition(Role condition, int page, int size);
    
    // 统计符合条件的角色数量
    long countByCondition(Role condition);
    
    // 删除角色
    void deleteById(String id);
    
    // 更新角色状态
    void updateStatus(String id, String status);
    
    // 检查角色编码是否存在
    boolean existsByCode(String code);
    
    // 根据用户ID查询角色列表
    List<Role> findByUserId(String userId);
} 