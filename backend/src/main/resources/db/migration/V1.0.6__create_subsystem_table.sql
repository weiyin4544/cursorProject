-- 创建子系统表
CREATE TABLE t_subsystem (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '子系统名称',
    code VARCHAR(50) NOT NULL COMMENT '子系统编码',
    type VARCHAR(20) NOT NULL COMMENT '类型：ECHAT, PDT, BTRUNC, ICS',
    connection_info TEXT COMMENT '连接信息',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-启用，INACTIVE-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='子系统表';

-- 初始化子系统数据
INSERT INTO t_subsystem (id, name, code, type, connection_info, status, create_time, update_time)
VALUES
('1', 'eChat系统', 'ECHAT_001', 'ECHAT', '{"serverUrl":"https://echat.example.com","apiKey":"echat_api_key"}', 'ACTIVE', NOW(), NOW()),
('2', 'PDT系统', 'PDT_001', 'PDT', '{"serverUrl":"https://pdt.example.com","apiKey":"pdt_api_key"}', 'ACTIVE', NOW(), NOW()),
('3', 'BTrunC系统', 'BTRUNC_001', 'BTRUNC', '{"serverUrl":"https://btrunc.example.com","apiKey":"btrunc_api_key"}', 'ACTIVE', NOW(), NOW()),
('4', 'iCS系统', 'ICS_001', 'ICS', '{"serverUrl":"https://ics.example.com","apiKey":"ics_api_key"}', 'ACTIVE', NOW(), NOW()); 