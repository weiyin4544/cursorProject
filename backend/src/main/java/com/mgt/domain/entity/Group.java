package com.mgt.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 群组实体
 */
@Data
public class Group {
    /**
     * 主键ID
     */
    private String id;

    /**
     * 群组名称
     */
    private String name;

    /**
     * 群组编码
     */
    private String code;

    /**
     * 所属组织ID
     */
    private String organizationId;

    /**
     * 群组类型：PATCH-派接组，NATIVE-原生组，LOCAL-本地组
     */
    private String type;

    /**
     * 群组描述
     */
    private String description;

    /**
     * 状态：ENABLED-启用，DISABLED-禁用
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