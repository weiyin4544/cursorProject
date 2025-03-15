package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Role;
import com.mgt.domain.service.RoleService;
import com.mgt.infrastructure.config.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public Result<Role> createRole(@RequestBody Role role) {
        return Result.success(roleService.createRole(role));
    }

    @PutMapping("/{id}")
    public Result<Role> updateRole(@PathVariable String id, @RequestBody Role role) {
        return Result.success(roleService.updateRole(id, role));
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable String id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<Role> getRole(@PathVariable String id) {
        return Result.success(roleService.getRole(id));
    }

    @GetMapping
    public Result<Map<String, Object>> getRoleList(Role condition,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(defaultValue = "10") int size) {
        return Result.success(roleService.getRoleList(condition, page, size));
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(@PathVariable String id, @RequestParam String status) {
        roleService.updateStatus(id, status);
        return Result.success();
    }

    @PostMapping("/{id}/permissions")
    public Result<Void> assignPermissions(@PathVariable String id, @RequestBody List<String> permissionIds) {
        roleService.assignPermissions(id, permissionIds);
        return Result.success();
    }

    @GetMapping("/{id}/permissions")
    public Result<List<String>> getRolePermissions(@PathVariable String id) {
        return Result.success(roleService.getRolePermissions(id));
    }

    @GetMapping("/all")
    public Result<List<Role>> getAllRoles() {
        return Result.success(roleService.getAllRoles());
    }

    @GetMapping("/check-code")
    public Result<Boolean> checkCode(@RequestParam String code) {
        return Result.success(roleService.isCodeExists(code));
    }
} 