package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Permission;
import com.mgt.domain.service.PermissionService;
import com.mgt.infrastructure.config.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public Result<Permission> createPermission(@RequestBody Permission permission) {
        return Result.success(permissionService.createPermission(permission));
    }

    @PutMapping("/{id}")
    public Result<Permission> updatePermission(@PathVariable String id, @RequestBody Permission permission) {
        return Result.success(permissionService.updatePermission(id, permission));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deletePermission(@PathVariable String id) {
        permissionService.deletePermission(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Permission> getPermission(@PathVariable String id) {
        return Result.success(permissionService.getPermission(id));
    }

    @GetMapping
    public Result<List<Permission>> getPermissionList(Permission condition) {
        return Result.success(permissionService.getPermissionList(condition));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable String id, @RequestParam String status) {
        permissionService.updateStatus(id, status);
        return Result.success();
    }

    @GetMapping("/tree")
    public Result<List<Permission>> getPermissionTree() {
        return Result.success(permissionService.getPermissionTree());
    }

    @GetMapping("/role/{roleId}")
    public Result<List<Permission>> getPermissionsByRoleId(@PathVariable String roleId) {
        return Result.success(permissionService.getPermissionsByRoleId(roleId));
    }

    @GetMapping("/user/{userId}")
    public Result<List<Permission>> getPermissionsByUserId(@PathVariable String userId) {
        return Result.success(permissionService.getPermissionsByUserId(userId));
    }

    @GetMapping("/check-code")
    public Result<Boolean> checkCode(@RequestParam String code) {
        return Result.success(permissionService.isCodeExists(code));
    }
} 