package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 派接组与原生组关联实体
 */
@Data
public class PatchNativeGroup {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 派接组ID
     */
    private String patchGroupId;

    /**
     * 原生组ID
     */
    private String nativeGroupId;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
} 