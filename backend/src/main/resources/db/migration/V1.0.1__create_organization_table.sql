CREATE TABLE t_organization (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '组织名称',
    code VARCHAR(50) NOT NULL COMMENT '组织编码',
    description VARCHAR(500) COMMENT '组织描述',
    parent_id VARCHAR(36) COMMENT '父组织ID',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织表'; 