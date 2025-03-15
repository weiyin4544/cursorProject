package com.mgt.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Organization {
    private String id;
    private String name;
    private String code;
    private String description;
    private String parentId;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Organization> children;
    
    // 非持久化字段
    private String parentName;
} 