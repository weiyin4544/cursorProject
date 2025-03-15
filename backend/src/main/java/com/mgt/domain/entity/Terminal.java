package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 终端实体
 */
@Data
public class Terminal {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 终端名称
     */
    private String name;

    /**
     * 终端编码
     */
    private String code;

    /**
     * 所属组织ID
     */
    private String organizationId;

    /**
     * 所属子系统ID
     */
    private String subsystemId;

    /**
     * 使用人员ID
     */
    private String personId;

    /**
     * 终端型号
     */
    private String model;

    /**
     * 序列号
     */
    private String serialNumber;

    /**
     * 状态：ONLINE-在线，OFFLINE-离线
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