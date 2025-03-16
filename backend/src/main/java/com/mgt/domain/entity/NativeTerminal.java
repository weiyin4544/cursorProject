package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 原生组与终端关联实体
 */
@Data
public class NativeTerminal {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 原生组ID
     */
    private String nativeGroupId;

    /**
     * 终端ID
     */
    private String terminalId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 