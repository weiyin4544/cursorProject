package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 调度员与子系统关联实体
 */
@Data
public class DispatcherSubsystem {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 调度员ID
     */
    private String dispatcherId;

    /**
     * 子系统ID
     */
    private String subsystemId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 