-- 创建调度员表
CREATE TABLE IF NOT EXISTS `t_dispatcher` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '调度员名称',
    `code` VARCHAR(50) NOT NULL COMMENT '调度员编码',
    `organization_id` VARCHAR(36) NOT NULL COMMENT '所属组织ID',
    `user_id` VARCHAR(36) COMMENT '关联用户ID',
    `status` VARCHAR(20) NOT NULL COMMENT '状态：ACTIVE-活跃，INACTIVE-非活跃',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调度员表';

-- 创建调度员与子系统关联表
CREATE TABLE IF NOT EXISTS `t_dispatcher_subsystem` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `dispatcher_id` VARCHAR(36) NOT NULL COMMENT '调度员ID',
    `subsystem_id` VARCHAR(36) NOT NULL COMMENT '子系统ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_dispatcher_subsystem` (`dispatcher_id`, `subsystem_id`),
    KEY `idx_dispatcher_id` (`dispatcher_id`),
    KEY `idx_subsystem_id` (`subsystem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调度员与子系统关联表';

-- 初始化调度员数据
INSERT INTO `t_dispatcher` (`id`, `name`, `code`, `organization_id`, `user_id`, `status`, `create_time`, `update_time`) VALUES
('d001', '调度员001', 'DISP001', 'org001', 'user001', 'ACTIVE', NOW(), NOW()),
('d002', '调度员002', 'DISP002', 'org001', 'user002', 'ACTIVE', NOW(), NOW()),
('d003', '调度员003', 'DISP003', 'org002', 'user003', 'ACTIVE', NOW(), NOW()),
('d004', '调度员004', 'DISP004', 'org002', 'user004', 'INACTIVE', NOW(), NOW());

-- 初始化调度员与子系统关联数据
INSERT INTO `t_dispatcher_subsystem` (`id`, `dispatcher_id`, `subsystem_id`, `create_time`) VALUES
('ds001', 'd001', 's001', NOW()),
('ds002', 'd001', 's002', NOW()),
('ds003', 'd002', 's001', NOW()),
('ds004', 'd003', 's003', NOW()),
('ds005', 'd004', 's004', NOW()); 