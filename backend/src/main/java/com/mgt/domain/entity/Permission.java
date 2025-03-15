package com.mgt.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Permission {
    private String id;
    private String name;
    private String code;
    private String type;
    private String parentId;
    private String path;
    private String component;
    private String redirect;
    private String icon;
    private Integer sort;
    private Boolean hidden;
    private Boolean keepAlive;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    
    // 非持久化字段
    private String parentName;
} 