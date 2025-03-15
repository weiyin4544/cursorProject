package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Permission;
import com.mgt.domain.repository.PermissionRepository;
import com.mgt.domain.repository.RolePermissionRepository;
import com.mgt.domain.service.PermissionService;
import com.mgt.infrastructure.config.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    @Override
    @Transactional
    public Permission createPermission(Permission permission) {
        // 检查编码是否存在
        if (isCodeExists(permission.getCode())) {
            throw new BusinessException("权限编码已存在");
        }
        
        // 如果有父权限,检查父权限是否存在
        if (permission.getParentId() != null) {
            permissionRepository.findById(permission.getParentId())
                .orElseThrow(() -> new BusinessException("父权限不存在"));
        }
        
        // 设置基本信息
        permission.setId(UUID.randomUUID().toString());
        permission.setStatus("1"); // 默认启用
        permission.setCreateTime(LocalDateTime.now());
        permission.setUpdateTime(LocalDateTime.now());
        
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public Permission updatePermission(String id, Permission permission) {
        // 检查权限是否存在
        Permission existingPermission = permissionRepository.findById(id)
            .orElseThrow(() -> new BusinessException("权限不存在"));
        
        // 检查编码是否存在
        if (!existingPermission.getCode().equals(permission.getCode()) && isCodeExists(permission.getCode())) {
            throw new BusinessException("权限编码已存在");
        }
        
        // 如果有父权限,检查父权限是否存在
        if (permission.getParentId() != null && !permission.getParentId().equals(existingPermission.getParentId())) {
            permissionRepository.findById(permission.getParentId())
                .orElseThrow(() -> new BusinessException("父权限不存在"));
        }
        
        // 更新基本信息
        permission.setId(id);
        permission.setStatus(existingPermission.getStatus());
        permission.setUpdateTime(LocalDateTime.now());
        permission.setCreateTime(existingPermission.getCreateTime());
        
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(String id) {
        // 检查权限是否存在
        permissionRepository.findById(id)
            .orElseThrow(() -> new BusinessException("权限不存在"));
        
        // 检查是否存在子权限
        if (permissionRepository.hasChildren(id)) {
            throw new BusinessException("存在子权限,不能删除");
        }
        
        // 删除角色权限关联
        rolePermissionRepository.deleteByPermissionId(id);
        
        // 删除权限
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission getPermission(String id) {
        return permissionRepository.findById(id)
            .orElseThrow(() -> new BusinessException("权限不存在"));
    }

    @Override
    public List<Permission> getPermissionList(Permission condition) {
        return permissionRepository.findByCondition(condition);
    }

    @Override
    @Transactional
    public void updateStatus(String id, String status) {
        // 检查权限是否存在
        permissionRepository.findById(id)
            .orElseThrow(() -> new BusinessException("权限不存在"));
        
        permissionRepository.updateStatus(id, status);
    }

    @Override
    public boolean isCodeExists(String code) {
        return permissionRepository.existsByCode(code);
    }

    @Override
    public List<Permission> getPermissionTree() {
        return permissionRepository.findTree();
    }

    @Override
    public List<Permission> getPermissionsByRoleId(String roleId) {
        return permissionRepository.findByRoleId(roleId);
    }

    @Override
    public List<Permission> getPermissionsByUserId(String userId) {
        return permissionRepository.findByUserId(userId);
    }
} 