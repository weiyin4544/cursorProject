package com.mgt.domain.service.impl;

import com.mgt.domain.entity.Department;
import com.mgt.domain.repository.DepartmentRepository;
import com.mgt.domain.service.DepartmentService;
import com.mgt.infrastructure.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门服务实现类
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public Department createDepartment(Department department) {
        // 检查部门编码是否已存在
        if (checkCodeExists(department.getCode())) {
            throw new BusinessException("部门编码已存在");
        }

        // 设置创建时间和更新时间
        department.setCreateTime(LocalDateTime.now());
        department.setUpdateTime(LocalDateTime.now());

        // 如果状态为空，设置为启用
        if (department.getStatus() == null) {
            department.setStatus("1");
        }

        // 如果排序号为空，设置为0
        if (department.getSort() == null) {
            department.setSort(0);
        }

        return departmentRepository.save(department);
    }

    @Override
    @Transactional
    public Department updateDepartment(String id, Department department) {
        // 检查部门是否存在
        Department existingDepartment = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        // 检查部门编码是否已存在（排除自身）
        if (checkCodeExists(department.getCode(), id)) {
            throw new BusinessException("部门编码已存在");
        }

        // 更新部门信息
        existingDepartment.update(
                department.getName(),
                department.getCode(),
                department.getOrganizationId(),
                department.getParentId(),
                department.getManagerId(),
                department.getDescription(),
                department.getSort()
        );

        return departmentRepository.save(existingDepartment);
    }

    @Override
    @Transactional
    public void deleteDepartment(String id) {
        // 检查部门是否存在
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        // 检查是否有子部门
        if (departmentRepository.hasChildren(id)) {
            throw new BusinessException("该部门下有子部门，无法删除");
        }

        // 删除部门
        departmentRepository.deleteById(id);
    }

    @Override
    public Optional<Department> getDepartment(String id) {
        return departmentRepository.findById(id);
    }

    @Override
    public List<Department> getDepartmentList(Department condition) {
        return departmentRepository.findByCondition(condition);
    }

    @Override
    public List<Map<String, Object>> getDepartmentTree() {
        List<Department> departments = departmentRepository.findAll();
        return buildDepartmentTree(departments, null);
    }

    @Override
    public List<Map<String, Object>> getDepartmentTreeByOrganizationId(String organizationId) {
        List<Department> departments = departmentRepository.findByOrganizationId(organizationId);
        return buildDepartmentTree(departments, null);
    }

    /**
     * 构建部门树
     *
     * @param departments 部门列表
     * @param parentId 父部门ID
     * @return 部门树
     */
    private List<Map<String, Object>> buildDepartmentTree(List<Department> departments, String parentId) {
        return departments.stream()
                .filter(dept -> {
                    if (parentId == null) {
                        return dept.getParentId() == null;
                    } else {
                        return parentId.equals(dept.getParentId());
                    }
                })
                .map(dept -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", dept.getId());
                    map.put("name", dept.getName());
                    map.put("code", dept.getCode());
                    map.put("description", dept.getDescription());
                    map.put("organizationId", dept.getOrganizationId());
                    map.put("parentId", dept.getParentId());
                    map.put("managerId", dept.getManagerId());
                    map.put("status", dept.getStatus());
                    map.put("sort", dept.getSort());
                    map.put("createTime", dept.getCreateTime());
                    map.put("updateTime", dept.getUpdateTime());

                    List<Map<String, Object>> children = buildDepartmentTree(departments, dept.getId());
                    if (!children.isEmpty()) {
                        map.put("children", children);
                    }

                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Department updateStatus(String id, String status) {
        // 检查部门是否存在
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("部门不存在"));

        // 更新状态
        if ("1".equals(status)) {
            department.enable();
        } else if ("0".equals(status)) {
            department.disable();
        } else {
            throw new BusinessException("无效的状态值");
        }

        return departmentRepository.save(department);
    }

    @Override
    public boolean checkCodeExists(String code) {
        return departmentRepository.findByCode(code).isPresent();
    }

    @Override
    public boolean checkCodeExists(String code, String excludeId) {
        Optional<Department> departmentOpt = departmentRepository.findByCode(code);
        return departmentOpt.isPresent() && !departmentOpt.get().getId().equals(excludeId);
    }
} 