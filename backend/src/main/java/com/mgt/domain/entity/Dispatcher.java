package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 调度员实体
 */
@Data
public class Dispatcher {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 调度员名称
     */
    private String name;

    /**
     * 调度员编码
     */
    private String code;

    /**
     * 所属组织ID
     */
    private String organizationId;

    /**
     * 关联用户ID
     */
    private String userId;

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