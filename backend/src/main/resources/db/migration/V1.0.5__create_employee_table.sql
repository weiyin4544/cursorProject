-- 创建员工表
CREATE TABLE IF NOT EXISTS `t_employee` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `gender` VARCHAR(10) COMMENT '性别',
    `birth_date` DATE COMMENT '出生日期',
    `id_card` VARCHAR(18) COMMENT '身份证号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `address` VARCHAR(255) COMMENT '地址',
    `department_id` VARCHAR(36) COMMENT '部门ID',
    `organization_id` VARCHAR(36) COMMENT '组织ID',
    `position` VARCHAR(50) COMMENT '职位',
    `hire_date` DATE COMMENT '入职日期',
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_employee_department` (`department_id`),
    INDEX `idx_employee_organization` (`organization_id`),
    INDEX `idx_employee_name` (`name`),
    INDEX `idx_employee_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- 创建电话号码表
CREATE TABLE IF NOT EXISTS `t_phone_number` (
    `id` VARCHAR(36) NOT NULL COMMENT '主键ID',
    `employee_id` VARCHAR(36) NOT NULL COMMENT '员工ID',
    `phone_number` VARCHAR(20) NOT NULL COMMENT '电话号码',
    `type` VARCHAR(20) NOT NULL DEFAULT 'MOBILE' COMMENT '类型：MOBILE-手机，OFFICE-办公电话，HOME-家庭电话',
    `is_primary` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否主要联系方式',
    `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_phone_employee` (`employee_id`),
    INDEX `idx_phone_number` (`phone_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='电话号码表';

-- 初始化测试数据
INSERT INTO `t_employee` (`id`, `name`, `gender`, `birth_date`, `id_card`, `email`, `address`, `department_id`, `organization_id`, `position`, `hire_date`, `status`)
VALUES
('1', '张三', '男', '1990-01-01', '110101199001010011', 'zhangsan@example.com', '北京市朝阳区', '1', '1', '工程师', '2020-01-01', 'ACTIVE'),
('2', '李四', '女', '1992-02-02', '110101199202020022', 'lisi@example.com', '北京市海淀区', '2', '1', '产品经理', '2020-02-01', 'ACTIVE'),
('3', '王五', '男', '1988-03-03', '110101198803030033', 'wangwu@example.com', '北京市西城区', '3', '1', '设计师', '2020-03-01', 'ACTIVE');

INSERT INTO `t_phone_number` (`id`, `employee_id`, `phone_number`, `type`, `is_primary`)
VALUES
('1', '1', '13800138001', 'MOBILE', 1),
('2', '1', '010-12345678', 'OFFICE', 0),
('3', '2', '13900139001', 'MOBILE', 1),
('4', '3', '13700137001', 'MOBILE', 1); 