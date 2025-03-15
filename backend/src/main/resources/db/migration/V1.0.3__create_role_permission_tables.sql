-- 角色表
CREATE TABLE t_role (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    code VARCHAR(50) NOT NULL COMMENT '角色编码',
    description VARCHAR(500) COMMENT '角色描述',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE t_permission (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL COMMENT '权限名称',
    code VARCHAR(50) NOT NULL COMMENT '权限编码',
    type VARCHAR(20) NOT NULL COMMENT '权限类型：menu-菜单，button-按钮',
    parent_id VARCHAR(36) COMMENT '父权限ID',
    path VARCHAR(200) COMMENT '路由路径',
    component VARCHAR(200) COMMENT '组件路径',
    redirect VARCHAR(200) COMMENT '重定向路径',
    icon VARCHAR(50) COMMENT '图标',
    sort INT COMMENT '排序',
    hidden TINYINT(1) DEFAULT 0 COMMENT '是否隐藏：0-否，1-是',
    keep_alive TINYINT(1) DEFAULT 0 COMMENT '是否缓存：0-否，1-是',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间',
    UNIQUE KEY uk_code (code),
    KEY idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE t_user_role (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL COMMENT '用户ID',
    role_id VARCHAR(36) NOT NULL COMMENT '角色ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    KEY idx_user_id (user_id),
    KEY idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE t_role_permission (
    id VARCHAR(36) PRIMARY KEY,
    role_id VARCHAR(36) NOT NULL COMMENT '角色ID',
    permission_id VARCHAR(36) NOT NULL COMMENT '权限ID',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    KEY idx_role_id (role_id),
    KEY idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 初始化管理员角色
INSERT INTO t_role (id, name, code, description, status, create_time, update_time)
VALUES (
    'admin',
    '管理员',
    'ADMIN',
    '系统管理员角色',
    '1',
    NOW(),
    NOW()
);

-- 初始化用户角色关联
INSERT INTO t_user_role (id, user_id, role_id, create_time)
VALUES (
    'admin_role',
    'admin',
    'admin',
    NOW()
); 