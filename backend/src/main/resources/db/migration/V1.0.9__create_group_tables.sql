-- 创建群组表
CREATE TABLE IF NOT EXISTS `t_group` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '群组名称',
    `code` VARCHAR(50) NOT NULL COMMENT '群组编码',
    `organization_id` VARCHAR(36) NOT NULL COMMENT '所属组织ID',
    `type` VARCHAR(20) NOT NULL COMMENT '群组类型：PATCH-派接组，NATIVE-原生组，LOCAL-本地组',
    `description` VARCHAR(500) COMMENT '群组描述',
    `status` VARCHAR(20) NOT NULL COMMENT '状态：ENABLED-启用，DISABLED-禁用',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    `update_time` DATETIME NOT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`),
    KEY `idx_organization_id` (`organization_id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='群组表';

-- 创建派接组与原生组关联表
CREATE TABLE IF NOT EXISTS `t_patch_native_group` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `patch_group_id` VARCHAR(36) NOT NULL COMMENT '派接组ID',
    `native_group_id` VARCHAR(36) NOT NULL COMMENT '原生组ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_patch_native` (`patch_group_id`, `native_group_id`),
    KEY `idx_patch_group_id` (`patch_group_id`),
    KEY `idx_native_group_id` (`native_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='派接组与原生组关联表';

-- 创建原生组与终端关联表
CREATE TABLE IF NOT EXISTS `t_native_terminal` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `native_group_id` VARCHAR(36) NOT NULL COMMENT '原生组ID',
    `terminal_id` VARCHAR(36) NOT NULL COMMENT '终端ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_native_terminal` (`native_group_id`, `terminal_id`),
    KEY `idx_native_group_id` (`native_group_id`),
    KEY `idx_terminal_id` (`terminal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='原生组与终端关联表';

-- 创建本地组与调度员关联表
CREATE TABLE IF NOT EXISTS `t_local_dispatcher` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `local_group_id` VARCHAR(36) NOT NULL COMMENT '本地组ID',
    `dispatcher_id` VARCHAR(36) NOT NULL COMMENT '调度员ID',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_local_dispatcher` (`local_group_id`, `dispatcher_id`),
    KEY `idx_local_group_id` (`local_group_id`),
    KEY `idx_dispatcher_id` (`dispatcher_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='本地组与调度员关联表';

-- 初始化群组数据
INSERT INTO `t_group` (`id`, `name`, `code`, `organization_id`, `type`, `description`, `status`, `create_time`, `update_time`) VALUES
-- 派接组
('g001', '应急指挥派接组', 'PATCH001', 'org001', 'PATCH', '用于应急指挥的派接组', 'ENABLED', NOW(), NOW()),
('g002', '日常调度派接组', 'PATCH002', 'org001', 'PATCH', '用于日常调度的派接组', 'ENABLED', NOW(), NOW()),
-- 原生组
('g003', 'eChat原生组1', 'NATIVE001', 'org001', 'NATIVE', 'eChat系统的原生组', 'ENABLED', NOW(), NOW()),
('g004', 'PDT原生组1', 'NATIVE002', 'org001', 'NATIVE', 'PDT系统的原生组', 'ENABLED', NOW(), NOW()),
('g005', 'BTrunC原生组1', 'NATIVE003', 'org002', 'NATIVE', 'BTrunC系统的原生组', 'ENABLED', NOW(), NOW()),
-- 本地组
('g006', '调度员本地组1', 'LOCAL001', 'org001', 'LOCAL', '调度员之间的通信组', 'ENABLED', NOW(), NOW()),
('g007', '调度员本地组2', 'LOCAL002', 'org002', 'LOCAL', '调度员之间的通信组', 'ENABLED', NOW(), NOW());

-- 初始化派接组与原生组关联数据
INSERT INTO `t_patch_native_group` (`id`, `patch_group_id`, `native_group_id`, `create_time`) VALUES
('pn001', 'g001', 'g003', NOW()),
('pn002', 'g001', 'g004', NOW()),
('pn003', 'g002', 'g003', NOW()),
('pn004', 'g002', 'g005', NOW());

-- 初始化原生组与终端关联数据
INSERT INTO `t_native_terminal` (`id`, `native_group_id`, `terminal_id`, `create_time`) VALUES
('nt001', 'g003', 't001', NOW()),
('nt002', 'g003', 't002', NOW()),
('nt003', 'g004', 't003', NOW()),
('nt004', 'g005', 't004', NOW()),
('nt005', 'g005', 't005', NOW());

-- 初始化本地组与调度员关联数据
INSERT INTO `t_local_dispatcher` (`id`, `local_group_id`, `dispatcher_id`, `create_time`) VALUES
('ld001', 'g006', 'd001', NOW()),
('ld002', 'g006', 'd002', NOW()),
('ld003', 'g007', 'd003', NOW()),
('ld004', 'g007', 'd004', NOW()); 