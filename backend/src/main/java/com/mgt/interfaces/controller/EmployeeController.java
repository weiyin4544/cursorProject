package com.mgt.interfaces.controller;

import com.mgt.domain.entity.Employee;
import com.mgt.domain.service.EmployeeService;
import com.mgt.interfaces.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * 员工控制器
 */
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * 创建员工
     *
     * @param employee 员工实体
     * @return 创建结果
     */
    @PostMapping
    public Result<Employee> createEmployee(@RequestBody Employee employee) {
        try {
            Employee createdEmployee = employeeService.createEmployee(employee);
            return Result.success(createdEmployee);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 更新员工
     *
     * @param id 员工ID
     * @param employee 员工实体
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<Employee> updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, employee);
            return Result.success(updatedEmployee);
        } catch (NoSuchElementException e) {
            return Result.failure(e.getMessage());
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据ID查询员工
     *
     * @param id 员工ID
     * @return 查询结果
     */
    @GetMapping("/{id}")
    public Result<Employee> getEmployeeById(@PathVariable String id) {
        try {
            Optional<Employee> employee = employeeService.getEmployeeById(id);
            return employee.map(Result::success)
                    .orElseGet(() -> Result.failure("Employee not found with id: " + id));
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据条件查询员工
     *
     * @param employee 查询条件
     * @return 查询结果
     */
    @GetMapping("/search")
    public Result<List<Employee>> getEmployeesByCondition(Employee employee) {
        try {
            List<Employee> employees = employeeService.getEmployeesByCondition(employee);
            return Result.success(employees);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据部门ID查询员工
     *
     * @param departmentId 部门ID
     * @return 查询结果
     */
    @GetMapping("/department/{departmentId}")
    public Result<List<Employee>> getEmployeesByDepartmentId(@PathVariable String departmentId) {
        try {
            List<Employee> employees = employeeService.getEmployeesByDepartmentId(departmentId);
            return Result.success(employees);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据组织ID查询员工
     *
     * @param organizationId 组织ID
     * @return 查询结果
     */
    @GetMapping("/organization/{organizationId}")
    public Result<List<Employee>> getEmployeesByOrganizationId(@PathVariable String organizationId) {
        try {
            List<Employee> employees = employeeService.getEmployeesByOrganizationId(organizationId);
            return Result.success(employees);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 根据ID删除员工
     *
     * @param id 员工ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteEmployee(@PathVariable String id) {
        try {
            employeeService.deleteEmployee(id);
            return Result.success();
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 更新员工状态
     *
     * @param id 员工ID
     * @param status 状态
     * @return 更新结果
     */
    @PutMapping("/{id}/status")
    public Result<Employee> updateEmployeeStatus(@PathVariable String id, @RequestParam String status) {
        try {
            Employee updatedEmployee = employeeService.updateEmployeeStatus(id, status);
            return Result.success(updatedEmployee);
        } catch (NoSuchElementException e) {
            return Result.failure(e.getMessage());
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

    /**
     * 分页查询员工
     *
     * @param employee 查询条件
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 查询结果
     */
    @GetMapping("/page")
    public Result<Map<String, Object>> getEmployeesByPage(Employee employee,
                                                         @RequestParam(defaultValue = "1") int pageNum,
                                                         @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Map<String, Object> result = employeeService.getEmployeesByPage(employee, pageNum, pageSize);
            return Result.success(result);
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }
} 