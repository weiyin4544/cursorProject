package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 子系统实体
 */
@Data
public class Subsystem {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 子系统名称
     */
    private String name;

    /**
     * 子系统编码
     */
    private String code;

    /**
     * 子系统类型：ECHAT, PDT, BTRUNC, ICS
     */
    private String type;

    /**
     * 连接信息，JSON格式
     */
    private String connectionInfo;

    /**
     * 状态：ACTIVE-启用，INACTIVE-禁用
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
} 