package com.mgt.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RolePermission {
    private String id;
    private String roleId;
    private String permissionId;
    private LocalDateTime createTime;
    
    // 非持久化字段
    private String roleName;
    private String permissionName;
} 