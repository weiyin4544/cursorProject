package com.mgt.domain.repository;

import com.mgt.domain.entity.Department;

import java.util.List;
import java.util.Optional;

/**
 * 部门仓储接口
 */
public interface DepartmentRepository {
    /**
     * 保存部门
     *
     * @param department 部门实体
     * @return 保存后的部门
     */
    Department save(Department department);

    /**
     * 根据ID查询部门
     *
     * @param id 部门ID
     * @return 部门实体
     */
    Optional<Department> findById(String id);

    /**
     * 根据编码查询部门
     *
     * @param code 部门编码
     * @return 部门实体
     */
    Optional<Department> findByCode(String code);

    /**
     * 查询所有部门
     *
     * @return 部门列表
     */
    List<Department> findAll();

    /**
     * 根据条件查询部门
     *
     * @param condition 查询条件
     * @return 部门列表
     */
    List<Department> findByCondition(Department condition);

    /**
     * 根据组织ID查询部门
     *
     * @param organizationId 组织ID
     * @return 部门列表
     */
    List<Department> findByOrganizationId(String organizationId);

    /**
     * 根据父部门ID查询子部门
     *
     * @param parentId 父部门ID
     * @return 子部门列表
     */
    List<Department> findByParentId(String parentId);

    /**
     * 构建部门树
     *
     * @return 部门树
     */
    List<Department> findTree();

    /**
     * 根据组织ID构建部门树
     *
     * @param organizationId 组织ID
     * @return 部门树
     */
    List<Department> findTreeByOrganizationId(String organizationId);

    /**
     * 根据ID删除部门
     *
     * @param id 部门ID
     */
    void deleteById(String id);

    /**
     * 检查是否有子部门
     *
     * @param id 部门ID
     * @return 是否有子部门
     */
    boolean hasChildren(String id);

    /**
     * 统计部门数量
     *
     * @return 部门数量
     */
    long count();
} 