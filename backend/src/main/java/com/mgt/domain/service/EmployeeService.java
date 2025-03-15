package com.mgt.domain.service;

import com.mgt.domain.entity.Employee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 员工服务接口
 */
public interface EmployeeService {
    /**
     * 创建员工
     *
     * @param employee 员工实体
     * @return 创建后的员工实体
     */
    Employee createEmployee(Employee employee);

    /**
     * 更新员工
     *
     * @param id 员工ID
     * @param employee 员工实体
     * @return 更新后的员工实体
     */
    Employee updateEmployee(String id, Employee employee);

    /**
     * 根据ID查询员工
     *
     * @param id 员工ID
     * @return 员工实体
     */
    Optional<Employee> getEmployeeById(String id);

    /**
     * 根据条件查询员工
     *
     * @param employee 查询条件
     * @return 员工列表
     */
    List<Employee> getEmployeesByCondition(Employee employee);

    /**
     * 根据部门ID查询员工
     *
     * @param departmentId 部门ID
     * @return 员工列表
     */
    List<Employee> getEmployeesByDepartmentId(String departmentId);

    /**
     * 根据组织ID查询员工
     *
     * @param organizationId 组织ID
     * @return 员工列表
     */
    List<Employee> getEmployeesByOrganizationId(String organizationId);

    /**
     * 根据ID删除员工
     *
     * @param id 员工ID
     */
    void deleteEmployee(String id);

    /**
     * 更新员工状态
     *
     * @param id 员工ID
     * @param status 状态
     * @return 更新后的员工实体
     */
    Employee updateEmployeeStatus(String id, String status);

    /**
     * 分页查询员工
     *
     * @param employee 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 员工列表和总数
     */
    Map<String, Object> getEmployeesByPage(Employee employee, int pageNum, int pageSize);
} 