package com.mgt.domain.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 电话号码实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PhoneNumber {
    /**
     * 主键ID
     */
    private String id;
    
    /**
     * 员工ID
     */
    private String employeeId;
    
    /**
     * 电话号码
     */
    private String phoneNumber;
    
    /**
     * 类型：MOBILE-手机，OFFICE-办公电话，HOME-家庭电话
     */
    private String type;
    
    /**
     * 是否主要联系方式
     */
    private Boolean isPrimary;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 