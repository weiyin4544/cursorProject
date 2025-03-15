package com.mgt.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 部门实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    /**
     * 部门ID
     */
    private String id;
    
    /**
     * 部门名称
     */
    private String name;
    
    /**
     * 部门编码
     */
    private String code;
    
    /**
     * 部门描述
     */
    private String description;
    
    /**
     * 所属组织ID
     */
    private String organizationId;
    
    /**
     * 父部门ID
     */
    private String parentId;
    
    /**
     * 部门负责人ID
     */
    private String managerId;
    
    /**
     * 部门状态：1-启用，0-禁用
     */
    private String status;
    
    /**
     * 排序号
     */
    private Integer sort;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 创建新部门
     */
    public static Department create(String name, String code, String organizationId, String parentId, String managerId, String description) {
        Department department = new Department();
        department.setName(name);
        department.setCode(code);
        department.setOrganizationId(organizationId);
        department.setParentId(parentId);
        department.setManagerId(managerId);
        department.setDescription(description);
        department.setStatus("1"); // 默认启用
        department.setSort(0); // 默认排序号
        department.setCreateTime(LocalDateTime.now());
        department.setUpdateTime(LocalDateTime.now());
        return department;
    }
    
    /**
     * 更新部门信息
     */
    public void update(String name, String code, String organizationId, String parentId, String managerId, String description, Integer sort) {
        this.name = name;
        this.code = code;
        this.organizationId = organizationId;
        this.parentId = parentId;
        this.managerId = managerId;
        this.description = description;
        this.sort = sort;
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 启用部门
     */
    public void enable() {
        this.status = "1";
        this.updateTime = LocalDateTime.now();
    }
    
    /**
     * 禁用部门
     */
    public void disable() {
        this.status = "0";
        this.updateTime = LocalDateTime.now();
    }
} 