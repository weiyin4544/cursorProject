package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Employee;
import com.mgt.domain.repository.EmployeeRepository;
import com.mgt.domain.service.EmployeeService;
import com.mgt.domain.service.PhoneNumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 员工服务实现类
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PhoneNumberService phoneNumberService;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, PhoneNumberService phoneNumberService) {
        this.employeeRepository = employeeRepository;
        this.phoneNumberService = phoneNumberService;
    }

    @Override
    @Transactional
    public Employee createEmployee(Employee employee) {
        // 生成ID
        employee.setId(UUID.randomUUID().toString());
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        employee.setCreateTime(now);
        employee.setUpdateTime(now);
        // 如果状态为空，设置为ACTIVE
        if (employee.getStatus() == null || employee.getStatus().isEmpty()) {
            employee.setStatus("ACTIVE");
        }
        return employeeRepository.save(employee);
    }

    @Override
    @Transactional
    public Employee updateEmployee(String id, Employee employee) {
        // 查询员工是否存在
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            Employee updatedEmployee = existingEmployee.get();
            // 更新员工信息
            updatedEmployee.setName(employee.getName());
            updatedEmployee.setGender(employee.getGender());
            updatedEmployee.setBirthDate(employee.getBirthDate());
            updatedEmployee.setIdCard(employee.getIdCard());
            updatedEmployee.setEmail(employee.getEmail());
            updatedEmployee.setAddress(employee.getAddress());
            updatedEmployee.setDepartmentId(employee.getDepartmentId());
            updatedEmployee.setOrganizationId(employee.getOrganizationId());
            updatedEmployee.setPosition(employee.getPosition());
            updatedEmployee.setHireDate(employee.getHireDate());
            updatedEmployee.setStatus(employee.getStatus());
            // 更新时间
            updatedEmployee.setUpdateTime(LocalDateTime.now());
            return employeeRepository.save(updatedEmployee);
        }
        throw new NoSuchElementException("Employee not found with id: " + id);
    }

    @Override
    public Optional<Employee> getEmployeeById(String id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> getEmployeesByCondition(Employee employee) {
        return employeeRepository.findByCondition(employee);
    }

    @Override
    public List<Employee> getEmployeesByDepartmentId(String departmentId) {
        return employeeRepository.findByDepartmentId(departmentId);
    }

    @Override
    public List<Employee> getEmployeesByOrganizationId(String organizationId) {
        return employeeRepository.findByOrganizationId(organizationId);
    }

    @Override
    @Transactional
    public void deleteEmployee(String id) {
        // 删除员工关联的电话号码
        phoneNumberService.deletePhoneNumbersByEmployeeId(id);
        // 删除员工
        employeeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Employee updateEmployeeStatus(String id, String status) {
        Optional<Employee> existingEmployee = employeeRepository.findById(id);
        if (existingEmployee.isPresent()) {
            return employeeRepository.updateStatus(id, status);
        }
        throw new NoSuchElementException("Employee not found with id: " + id);
    }

    @Override
    public Map<String, Object> getEmployeesByPage(Employee employee, int pageNum, int pageSize) {
        List<Employee> employees = employeeRepository.findByPage(employee, pageNum, pageSize);
        long total = employeeRepository.count(employee);
        Map<String, Object> result = new HashMap<>();
        result.put("list", employees);
        result.put("total", total);
        return result;
    }
} 