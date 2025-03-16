package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 本地组与调度员关联实体
 */
@Data
public class LocalDispatcher {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 本地组ID
     */
    private String localGroupId;

    /**
     * 调度员ID
     */
    private String dispatcherId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 