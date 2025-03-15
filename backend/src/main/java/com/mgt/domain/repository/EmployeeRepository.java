package com.mgt.domain.repository;

import com.mgt.domain.entity.Employee;

import java.util.List;
import java.util.Optional;

/**
 * 员工仓储接口
 */
public interface EmployeeRepository {
    /**
     * 保存员工
     *
     * @param employee 员工实体
     * @return 保存后的员工实体
     */
    Employee save(Employee employee);

    /**
     * 根据ID查询员工
     *
     * @param id 员工ID
     * @return 员工实体
     */
    Optional<Employee> findById(String id);

    /**
     * 查询所有员工
     *
     * @return 员工列表
     */
    List<Employee> findAll();

    /**
     * 根据条件查询员工
     *
     * @param employee 查询条件
     * @return 员工列表
     */
    List<Employee> findByCondition(Employee employee);

    /**
     * 根据部门ID查询员工
     *
     * @param departmentId 部门ID
     * @return 员工列表
     */
    List<Employee> findByDepartmentId(String departmentId);

    /**
     * 根据组织ID查询员工
     *
     * @param organizationId 组织ID
     * @return 员工列表
     */
    List<Employee> findByOrganizationId(String organizationId);

    /**
     * 根据ID删除员工
     *
     * @param id 员工ID
     */
    void deleteById(String id);

    /**
     * 更新员工状态
     *
     * @param id 员工ID
     * @param status 状态
     * @return 更新后的员工实体
     */
    Employee updateStatus(String id, String status);

    /**
     * 分页查询员工
     *
     * @param employee 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 员工列表
     */
    List<Employee> findByPage(Employee employee, int pageNum, int pageSize);

    /**
     * 查询员工总数
     *
     * @param employee 查询条件
     * @return 员工总数
     */
    long count(Employee employee);
} 