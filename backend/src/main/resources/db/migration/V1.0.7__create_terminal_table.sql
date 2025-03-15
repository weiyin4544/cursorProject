-- 创建终端表
CREATE TABLE t_terminal (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '终端名称',
    code VARCHAR(50) NOT NULL COMMENT '终端编码',
    organization_id VARCHAR(36) NOT NULL COMMENT '所属组织ID',
    subsystem_id VARCHAR(36) NOT NULL COMMENT '所属子系统ID',
    person_id VARCHAR(36) COMMENT '使用人员ID',
    model VARCHAR(100) COMMENT '终端型号',
    serial_number VARCHAR(100) COMMENT '序列号',
    status VARCHAR(20) NOT NULL DEFAULT 'OFFLINE' COMMENT '状态：ONLINE-在线，OFFLINE-离线',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='终端表';

-- 初始化终端数据
INSERT INTO t_terminal (id, name, code, organization_id, subsystem_id, person_id, model, serial_number, status, create_time, update_time)
VALUES
('1', 'eChat终端001', 'ECHAT_TERM_001', '1', '1', NULL, 'EC-100', 'SN12345678', 'ONLINE', NOW(), NOW()),
('2', 'eChat终端002', 'ECHAT_TERM_002', '1', '1', NULL, 'EC-100', 'SN12345679', 'OFFLINE', NOW(), NOW()),
('3', 'PDT终端001', 'PDT_TERM_001', '2', '2', NULL, 'PD-200', 'SN23456789', 'ONLINE', NOW(), NOW()),
('4', 'BTrunC终端001', 'BTRUNC_TERM_001', '3', '3', NULL, 'BT-300', 'SN34567890', 'OFFLINE', NOW(), NOW()),
('5', 'iCS终端001', 'ICS_TERM_001', '4', '4', NULL, 'IC-400', 'SN45678901', 'ONLINE', NOW(), NOW()); 