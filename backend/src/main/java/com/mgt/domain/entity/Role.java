package com.mgt.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Role {
    private String id;
    private String name;
    private String code;
    private String description;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 非持久化字段
    private List<String> permissions;
} 