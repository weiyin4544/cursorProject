package com.mgt.domain.service;

import com.mgt.domain.entity.Department;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 部门服务接口
 */
public interface DepartmentService {
    /**
     * 创建部门
     *
     * @param department 部门信息
     * @return 创建后的部门
     */
    Department createDepartment(Department department);

    /**
     * 更新部门
     *
     * @param id 部门ID
     * @param department 部门信息
     * @return 更新后的部门
     */
    Department updateDepartment(String id, Department department);

    /**
     * 删除部门
     *
     * @param id 部门ID
     */
    void deleteDepartment(String id);

    /**
     * 获取部门详情
     *
     * @param id 部门ID
     * @return 部门详情
     */
    Optional<Department> getDepartment(String id);

    /**
     * 获取部门列表
     *
     * @param condition 查询条件
     * @return 部门列表
     */
    List<Department> getDepartmentList(Department condition);

    /**
     * 获取部门树
     *
     * @return 部门树
     */
    List<Map<String, Object>> getDepartmentTree();

    /**
     * 根据组织ID获取部门树
     *
     * @param organizationId 组织ID
     * @return 部门树
     */
    List<Map<String, Object>> getDepartmentTreeByOrganizationId(String organizationId);

    /**
     * 更新部门状态
     *
     * @param id 部门ID
     * @param status 状态
     * @return 更新后的部门
     */
    Department updateStatus(String id, String status);

    /**
     * 检查部门编码是否存在
     *
     * @param code 部门编码
     * @return 是否存在
     */
    boolean checkCodeExists(String code);

    /**
     * 检查部门编码是否存在（排除指定ID）
     *
     * @param code 部门编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCodeExists(String code, String excludeId);
} 