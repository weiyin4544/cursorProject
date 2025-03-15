package com.mgt.domain.entity;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class User {
    private String id;
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String phone;
    private String avatar;
    private String organizationId;
    private String departmentId;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private LocalDateTime lastLoginTime;
    
    // 非持久化字段
    private String organizationName;
    private String departmentName;
    private List<String> roles;
    private List<String> permissions;
} 