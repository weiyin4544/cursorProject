CREATE TABLE t_user (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像',
    organization_id VARCHAR(36) COMMENT '组织ID',
    department_id VARCHAR(36) COMMENT '部门ID',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    last_login_time DATETIME COMMENT '最后登录时间',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_email (email),
    UNIQUE KEY uk_phone (phone),
    KEY idx_organization_id (organization_id),
    KEY idx_department_id (department_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 初始化管理员账号
INSERT INTO t_user (id, username, password, nickname, status, create_time, update_time)
VALUES (
    'admin',
    'admin',
    -- 密码为 admin123，使用SHA-256加密后的Base64编码
    'jZae727K08KaOmKSgOaGzww/XVqGr/PKEgIMkjrcbJI=',
    '系统管理员',
    '1',
    NOW(),
    NOW()
); 