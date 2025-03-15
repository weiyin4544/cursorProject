package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Department;
import com.mgt.domain.service.DepartmentService;
import com.mgt.infrastructure.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 部门控制器
 */
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 创建部门
     *
     * @param department 部门信息
     * @return 创建结果
     */
    @PostMapping
    public Result<Department> createDepartment(@RequestBody Department department) {
        Department createdDepartment = departmentService.createDepartment(department);
        return Result.success(createdDepartment);
    }

    /**
     * 更新部门
     *
     * @param id 部门ID
     * @param department 部门信息
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Department> updateDepartment(@PathVariable String id, @RequestBody Department department) {
        Department updatedDepartment = departmentService.updateDepartment(id, department);
        return Result.success(updatedDepartment);
    }

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return Result.success();
    }

    /**
     * 获取部门详情
     *
     * @param id 部门ID
     * @return 部门详情
     */
    @GetMapping("/{id}")
    public Result<Department> getDepartment(@PathVariable String id) {
        return departmentService.getDepartment(id)
                .map(Result::success)
                .orElse(Result.fail("部门不存在"));
    }

    /**
     * 获取部门列表
     *
     * @param condition 查询条件
     * @return 部门列表
     */
    @GetMapping
    public Result<List<Department>> getDepartmentList(Department condition) {
        List<Department> departments = departmentService.getDepartmentList(condition);
        return Result.success(departments);
    }

    /**
     * 获取部门树
     *
     * @return 部门树
     */
    @GetMapping("/tree")
    public Result<List<Map<String, Object>>> getDepartmentTree() {
        List<Map<String, Object>> tree = departmentService.getDepartmentTree();
        return Result.success(tree);
    }

    /**
     * 根据组织ID获取部门树
     *
     * @param organizationId 组织ID
     * @return 部门树
     */
    @GetMapping("/tree/organization/{organizationId}")
    public Result<List<Map<String, Object>>> getDepartmentTreeByOrganizationId(@PathVariable String organizationId) {
        List<Map<String, Object>> tree = departmentService.getDepartmentTreeByOrganizationId(organizationId);
        return Result.success(tree);
    }

    /**
     * 更新部门状态
     *
     * @param id 部门ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<Department> updateStatus(@PathVariable String id, @RequestParam String status) {
        Department department = departmentService.updateStatus(id, status);
        return Result.success(department);
    }

    /**
     * 检查部门编码是否存在
     *
     * @param code 部门编码
     * @return 检查结果
     */
    @GetMapping("/check-code")
    public Result<Boolean> checkCode(@RequestParam String code) {
        boolean exists = departmentService.checkCodeExists(code);
        return Result.success(exists);
    }

    /**
     * 检查部门编码是否存在（排除指定ID）
     *
     * @param code 部门编码
     * @param excludeId 排除的ID
     * @return 检查结果
     */
    @GetMapping("/check-code/{excludeId}")
    public Result<Boolean> checkCode(@RequestParam String code, @PathVariable String excludeId) {
        boolean exists = departmentService.checkCodeExists(code, excludeId);
        return Result.success(exists);
    }
} 