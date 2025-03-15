-- 创建部门表
CREATE TABLE t_department (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '部门名称',
    code VARCHAR(50) NOT NULL COMMENT '部门编码',
    description VARCHAR(500) COMMENT '部门描述',
    organization_id VARCHAR(36) COMMENT '所属组织ID',
    parent_id VARCHAR(36) COMMENT '父部门ID',
    manager_id VARCHAR(36) COMMENT '部门负责人ID',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    sort INT NOT NULL DEFAULT 0 COMMENT '排序号',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code),
    KEY idx_organization_id (organization_id),
    KEY idx_parent_id (parent_id),
    KEY idx_manager_id (manager_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';

-- 初始化测试数据
INSERT INTO t_department (id, name, code, description, organization_id, parent_id, manager_id, status, sort, create_time, update_time)
VALUES 
('1', '技术部', 'TECH', '负责公司技术研发', '1', NULL, NULL, '1', 1, NOW(), NOW()),
('2', '研发一组', 'DEV1', '负责核心系统研发', '1', '1', NULL, '1', 1, NOW(), NOW()),
('3', '研发二组', 'DEV2', '负责周边系统研发', '1', '1', NULL, '1', 2, NOW(), NOW()),
('4', '测试组', 'QA', '负责系统测试', '1', '1', NULL, '1', 3, NOW(), NOW()),
('5', '运维组', 'OPS', '负责系统运维', '1', '1', NULL, '1', 4, NOW(), NOW()); 