package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Role;
import com.mgt.domain.entity.RolePermission;
import com.mgt.domain.repository.RolePermissionRepository;
import com.mgt.domain.repository.RoleRepository;
import com.mgt.domain.service.RoleService;
import com.mgt.infrastructure.config.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public Role createRole(Role role) {
        // 检查编码是否存在
        if (isCodeExists(role.getCode())) {
            throw new BusinessException("角色编码已存在");
        }
        
        // 设置基本信息
        role.setId(UUID.randomUUID().toString());
        role.setStatus("1"); // 默认启用
        role.setCreateTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(String id, Role role) {
        // 检查角色是否存在
        Role existingRole = roleRepository.findById(id)
            .orElseThrow(() -> new BusinessException("角色不存在"));
        
        // 检查编码是否存在
        if (!existingRole.getCode().equals(role.getCode()) && isCodeExists(role.getCode())) {
            throw new BusinessException("角色编码已存在");
        }
        
        // 更新基本信息
        role.setId(id);
        role.setStatus(existingRole.getStatus());
        role.setUpdateTime(LocalDateTime.now());
        role.setCreateTime(existingRole.getCreateTime());
        
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void deleteRole(String id) {
        // 检查角色是否存在
        roleRepository.findById(id)
            .orElseThrow(() -> new BusinessException("角色不存在"));
        
        // 删除角色权限关联
        rolePermissionRepository.deleteByRoleId(id);
        
        // 删除角色
        roleRepository.deleteById(id);
    }

    @Override
    public Role getRole(String id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new BusinessException("角色不存在"));
    }

    @Override
    public Map<String, Object> getRoleList(Role condition, int page, int size) {
        List<Role> roles = roleRepository.findByCondition(condition, page, size);
        long total = roleRepository.countByCondition(condition);
        
        Map<String, Object> result = new HashMap<>();
        result.put("data", roles);
        result.put("total", total);
        
        return result;
    }

    @Override
    @Transactional
    public void updateStatus(String id, String status) {
        // 检查角色是否存在
        roleRepository.findById(id)
            .orElseThrow(() -> new BusinessException("角色不存在"));
        
        roleRepository.updateStatus(id, status);
    }

    @Override
    @Transactional
    public void assignPermissions(String roleId, List<String> permissionIds) {
        // 检查角色是否存在
        roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException("角色不存在"));
        
        // 删除原有权限
        rolePermissionRepository.deleteByRoleId(roleId);
        
        // 添加新权限
        if (permissionIds != null && !permissionIds.isEmpty()) {
            List<RolePermission> rolePermissions = permissionIds.stream()
                .map(permissionId -> {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setId(UUID.randomUUID().toString());
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId);
                    rolePermission.setCreateTime(LocalDateTime.now());
                    return rolePermission;
                })
                .collect(Collectors.toList());
            
            rolePermissionRepository.batchSave(rolePermissions);
        }
    }

    @Override
    public List<String> getRolePermissions(String roleId) {
        // 检查角色是否存在
        roleRepository.findById(roleId)
            .orElseThrow(() -> new BusinessException("角色不存在"));
        
        return rolePermissionRepository.findByRoleId(roleId).stream()
            .map(RolePermission::getPermissionId)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isCodeExists(String code) {
        return roleRepository.existsByCode(code);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> getRolesByUserId(String userId) {
        return roleRepository.findByUserId(userId);
    }
} 