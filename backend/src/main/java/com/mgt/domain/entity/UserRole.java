package com.mgt.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserRole {
    private String id;
    private String userId;
    private String roleId;
    private LocalDateTime createTime;
    
    // 非持久化字段
    private String username;
    private String roleName;
} 