# 业务模块关联逻辑

## 1. 核心实体关系

### 1.1 用户与权限管理
- **用户(User)**: 系统的基本操作者，拥有账号和密码
- **角色(Role)**: 权限的集合，用于批量授权
- **权限(Permission)**: 系统功能的最小操作单元
- **关联关系**:
  - 用户与角色是多对多关系，通过`t_user_role`表关联
  - 角色与权限是多对多关系，通过`t_role_permission`表关联
  - 用户通过角色间接获取权限

### 1.2 组织与部门管理
- **组织(Organization)**: 企业的组织架构，可以是公司、分公司等
- **部门(Department)**: 组织内的职能单元，如技术部、市场部等
- **关联关系**:
  - 组织可以有多个子组织，形成树形结构
  - 部门归属于组织，一个组织可以有多个部门
  - 部门可以有多个子部门，形成树形结构
  - 用户关联到组织，表示用户所属的组织
  - 部门相对独立，主要与人员关联

### 1.3 人员管理
- **人员(Person)**: 实际业务中的人员，区别于系统用户
- **电话号码(PhoneNumber)**: 人员的联系方式
- **关联关系**:
  - 人员归属于部门，一个部门可以有多个人员
  - 人员可以配置多个电话号码
  - 人员可以配置多个终端
  - 人员可以配置多个国标设备

### 1.4 终端与子系统管理
- **终端(Terminal)**: 通信设备，如对讲机、手机等
- **子系统(Subsystem)**: 通信系统类型，包括eChat、PDT、BTrunC、iCS
- **关联关系**:
  - 终端归属于组织，一个组织可以有多个终端
  - 终端归属于子系统，一个子系统可以有多个终端
  - 终端可以被分配给人员使用

### 1.5 群组管理
- **群组(Group)**: 通信群组，分为派接组、原生组、本地组
- **关联关系**:
  - 群组归属于组织，一个组织可以有多个群组
  - 派接组的成员是原生组
  - 原生组的成员是终端
  - 本地组的成员是调度员

### 1.6 调度员管理
- **调度员(Dispatcher)**: 负责调度通信的操作人员
- **关联关系**:
  - 调度员归属于组织，一个组织可以有多个调度员
  - 调度员可以配置为子系统调度员
  - 调度员可以作为本地组的成员

## 2. 详细关联逻辑

### 2.1 用户-角色-权限-组织关联
1. 用户创建时必须关联到一个组织
2. 用户可以被分配一个或多个角色
3. 角色包含一组权限
4. 用户的有效权限是其所有角色权限的并集
5. 组织可以设置默认角色，新用户自动获得该组织的默认角色

### 2.2 部门-人员关联
1. 部门是相对独立的实体，主要与人员关联
2. 人员必须归属于一个部门
3. 部门可以设置负责人，负责人是人员的一种特殊角色
4. 部门变更时，其下属人员的部门归属自动更新

### 2.3 人员-终端-国标设备关联
1. 人员可以配置多个电话号码，包括办公电话、手机等
2. 人员可以配置多个终端设备
3. 人员可以配置多个国标设备
4. 终端和国标设备可以在人员之间转移

### 2.4 终端-子系统-组织关联
1. 终端必须归属于一个组织
2. 终端必须归属于一个子系统(eChat、PDT、BTrunC、iCS)
3. 子系统类型决定了终端的通信能力和协议
4. 组织可以查看和管理其下所有终端

### 2.5 群组分类与成员管理
1. 派接组
   - 成员由原生组组成
   - 可以跨子系统通信
   - 归属于特定组织

2. 原生组
   - 成员由终端组成
   - 通常在同一子系统内
   - 归属于特定组织

3. 本地组
   - 成员由调度员组成
   - 用于调度员之间的通信
   - 归属于特定组织

### 2.6 调度员-子系统-群组关联
1. 调度员必须归属于一个组织
2. 调度员可以配置为一个或多个子系统的调度员
3. 调度员可以作为本地组的成员
4. 调度员可以管理派接组和原生组

## 3. 业务流程示例

### 3.1 新增人员及配置流程
1. 在特定部门下创建人员记录
2. 为人员配置电话号码
3. 为人员分配终端设备
4. 为人员配置国标设备
5. 将人员相关终端加入适当的原生组

### 3.2 群组通信建立流程
1. 创建原生组并添加终端成员
2. 创建派接组并添加原生组成员
3. 创建本地组并添加调度员成员
4. 配置调度员对派接组的调度权限

### 3.3 子系统管理流程
1. 创建子系统记录(eChat、PDT、BTrunC、iCS)
2. 为子系统配置参数和连接信息
3. 将终端归属到相应子系统
4. 配置子系统调度员

## 4. 数据模型扩展建议

### 4.1 人员模块
```sql
-- 人员表
CREATE TABLE t_person (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '人员姓名',
    gender CHAR(1) COMMENT '性别：M-男，F-女',
    id_card VARCHAR(18) COMMENT '身份证号',
    department_id VARCHAR(36) NOT NULL COMMENT '所属部门ID',
    position VARCHAR(100) COMMENT '职位',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-在职，0-离职',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
);

-- 电话号码表
CREATE TABLE t_phone_number (
    id VARCHAR(36) PRIMARY KEY,
    person_id VARCHAR(36) NOT NULL COMMENT '人员ID',
    number VARCHAR(20) NOT NULL COMMENT '电话号码',
    type VARCHAR(20) COMMENT '类型：OFFICE-办公电话，MOBILE-手机，HOME-家庭电话',
    is_default TINYINT(1) DEFAULT 0 COMMENT '是否默认号码',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
);
```

### 4.2 终端与子系统模块
```sql
-- 子系统表
CREATE TABLE t_subsystem (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '子系统名称',
    code VARCHAR(50) NOT NULL COMMENT '子系统编码',
    type VARCHAR(20) NOT NULL COMMENT '类型：ECHAT, PDT, BTRUNC, ICS',
    connection_info TEXT COMMENT '连接信息',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
);

-- 终端表
CREATE TABLE t_terminal (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '终端名称',
    code VARCHAR(50) NOT NULL COMMENT '终端编码',
    organization_id VARCHAR(36) NOT NULL COMMENT '所属组织ID',
    subsystem_id VARCHAR(36) NOT NULL COMMENT '所属子系统ID',
    person_id VARCHAR(36) COMMENT '使用人员ID',
    model VARCHAR(100) COMMENT '终端型号',
    serial_number VARCHAR(100) COMMENT '序列号',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-在线，0-离线',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
);

-- 国标设备表
CREATE TABLE t_gb_device (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '设备名称',
    code VARCHAR(50) NOT NULL COMMENT '设备编码',
    person_id VARCHAR(36) COMMENT '使用人员ID',
    device_type VARCHAR(50) COMMENT '设备类型',
    manufacturer VARCHAR(100) COMMENT '制造商',
    model VARCHAR(100) COMMENT '设备型号',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-在线，0-离线',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
);
```

### 4.3 群组模块
```sql
-- 群组表
CREATE TABLE t_group (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '群组名称',
    code VARCHAR(50) NOT NULL COMMENT '群组编码',
    organization_id VARCHAR(36) NOT NULL COMMENT '所属组织ID',
    type VARCHAR(20) NOT NULL COMMENT '类型：PATCH-派接组，NATIVE-原生组，LOCAL-本地组',
    description VARCHAR(500) COMMENT '群组描述',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-启用，0-禁用',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
);

-- 派接组-原生组关联表
CREATE TABLE t_patch_native_group (
    id VARCHAR(36) PRIMARY KEY,
    patch_group_id VARCHAR(36) NOT NULL COMMENT '派接组ID',
    native_group_id VARCHAR(36) NOT NULL COMMENT '原生组ID',
    create_time DATETIME NOT NULL COMMENT '创建时间'
);

-- 原生组-终端关联表
CREATE TABLE t_native_terminal (
    id VARCHAR(36) PRIMARY KEY,
    native_group_id VARCHAR(36) NOT NULL COMMENT '原生组ID',
    terminal_id VARCHAR(36) NOT NULL COMMENT '终端ID',
    create_time DATETIME NOT NULL COMMENT '创建时间'
);

-- 本地组-调度员关联表
CREATE TABLE t_local_dispatcher (
    id VARCHAR(36) PRIMARY KEY,
    local_group_id VARCHAR(36) NOT NULL COMMENT '本地组ID',
    dispatcher_id VARCHAR(36) NOT NULL COMMENT '调度员ID',
    create_time DATETIME NOT NULL COMMENT '创建时间'
);
```

### 4.4 调度员模块
```sql
-- 调度员表
CREATE TABLE t_dispatcher (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '调度员名称',
    code VARCHAR(50) NOT NULL COMMENT '调度员编码',
    organization_id VARCHAR(36) NOT NULL COMMENT '所属组织ID',
    user_id VARCHAR(36) COMMENT '关联用户ID',
    status CHAR(1) NOT NULL DEFAULT '1' COMMENT '状态：1-在线，0-离线',
    create_time DATETIME NOT NULL COMMENT '创建时间',
    update_time DATETIME NOT NULL COMMENT '更新时间'
);

-- 调度员-子系统关联表
CREATE TABLE t_dispatcher_subsystem (
    id VARCHAR(36) PRIMARY KEY,
    dispatcher_id VARCHAR(36) NOT NULL COMMENT '调度员ID',
    subsystem_id VARCHAR(36) NOT NULL COMMENT '子系统ID',
    create_time DATETIME NOT NULL COMMENT '创建时间'
);
```

## 5. 群组管理模块实现细节

### 5.1 群组类型及特性

#### 5.1.1 派接组(Patch Group)
- **定义**：跨子系统的通信群组，由多个原生组组成
- **特性**：
  - 可以包含来自不同子系统的原生组
  - 实现跨系统通信的桥梁
  - 由调度员进行管理和控制
- **业务规则**：
  - 派接组必须至少包含一个原生组
  - 派接组可以动态添加或移除原生组
  - 派接组状态变更会影响其下所有原生组的通信

#### 5.1.2 原生组(Native Group)
- **定义**：单一子系统内的通信群组，由多个终端组成
- **特性**：
  - 通常属于同一子系统
  - 成员之间可以直接通信
  - 可以被包含在派接组中实现跨系统通信
- **业务规则**：
  - 原生组必须至少包含一个终端
  - 原生组内的终端通常属于同一子系统
  - 原生组可以被多个派接组引用

#### 5.1.3 本地组(Local Group)
- **定义**：调度员之间的通信群组
- **特性**：
  - 成员仅限于调度员
  - 用于调度员之间的协调和通信
  - 不直接与终端通信
- **业务规则**：
  - 本地组必须至少包含一个调度员
  - 本地组成员可以动态调整
  - 本地组主要用于调度中心内部通信

### 5.2 群组管理核心功能

#### 5.2.1 群组创建与配置
- **创建流程**：
  1. 选择群组类型(派接组/原生组/本地组)
  2. 设置群组基本信息(名称、编码、描述等)
  3. 选择所属组织
  4. 根据群组类型选择成员(原生组/终端/调度员)
  5. 设置群组状态(启用/禁用)
  6. 保存群组信息

- **配置选项**：
  - 群组名称和编码(唯一标识)
  - 群组描述和附加信息
  - 成员管理(添加/移除)
  - 状态控制(启用/禁用)

#### 5.2.2 群组成员管理
- **派接组成员管理**：
  - 添加/移除原生组
  - 查看原生组详情
  - 查看原生组内终端列表

- **原生组成员管理**：
  - 添加/移除终端
  - 按组织筛选终端
  - 按状态筛选终端

- **本地组成员管理**：
  - 添加/移除调度员
  - 按组织筛选调度员
  - 按状态筛选调度员

#### 5.2.3 群组状态管理
- **状态类型**：
  - 启用：群组可以正常通信
  - 禁用：群组暂停通信功能
  - 临时禁用：群组在特定时间段内禁用

- **状态变更规则**：
  - 派接组禁用时，不影响原生组内部通信
  - 原生组禁用时，该原生组在所有派接组中不可用
  - 本地组禁用时，调度员之间无法通过该组通信

### 5.3 群组关联关系处理

#### 5.3.1 派接组与原生组关联
- **关联表**：`t_patch_native_group`
- **关联规则**：
  - 一个派接组可以关联多个原生组
  - 一个原生组可以被多个派接组关联
  - 关联和解除关联操作需记录时间戳

#### 5.3.2 原生组与终端关联
- **关联表**：`t_native_terminal`
- **关联规则**：
  - 一个原生组可以关联多个终端
  - 一个终端可以属于多个原生组
  - 关联和解除关联操作需记录时间戳
  - 终端状态变更会影响其在原生组中的可用性

#### 5.3.3 本地组与调度员关联
- **关联表**：`t_local_dispatcher`
- **关联规则**：
  - 一个本地组可以关联多个调度员
  - 一个调度员可以属于多个本地组
  - 关联和解除关联操作需记录时间戳
  - 调度员状态变更会影响其在本地组中的可用性

### 5.4 群组通信流程

#### 5.4.1 派接组通信流程
1. 调度员选择派接组发起通信
2. 系统识别派接组包含的所有原生组
3. 系统向各原生组所属子系统发送通信请求
4. 各子系统将通信内容分发给原生组内的终端
5. 终端接收通信内容并响应
6. 响应信息通过原生组返回到派接组
7. 调度员接收所有终端的响应信息

#### 5.4.2 原生组通信流程
1. 终端或调度员选择原生组发起通信
2. 系统向原生组所属子系统发送通信请求
3. 子系统将通信内容分发给原生组内的所有终端
4. 终端接收通信内容并响应
5. 响应信息返回给通信发起方

#### 5.4.3 本地组通信流程
1. 调度员选择本地组发起通信
2. 系统将通信内容分发给本地组内的所有调度员
3. 调度员接收通信内容并响应
4. 响应信息返回给通信发起方

## 6. 调度员管理模块实现细节

## 7. 后续模块实现要求

在实现后续模块时，需要遵循以下要求：

1. **人员模块**
   - 实现人员的基本信息管理
   - 支持人员与部门的关联
   - 支持人员配置多个电话号码
   - 支持人员关联终端和国标设备

2. **子系统模块**
   - 实现eChat、PDT、BTrunC、iCS四种子系统的管理
   - 支持子系统参数配置
   - 支持终端与子系统的关联

3. **终端模块**
   - 实现终端的基本信息管理
   - 支持终端与组织、子系统的关联
   - 支持终端分配给人员使用

4. **群组模块**
   - 实现派接组、原生组、本地组三种群组类型
   - 支持不同类型群组的成员管理
   - 支持群组与组织的关联

5. **调度员模块**
   - 实现调度员的基本信息管理
   - 支持调度员与组织的关联
   - 支持调度员配置为子系统调度员
   - 支持调度员作为本地组成员

所有模块实现时，需要考虑实体间的关联关系，确保数据一致性和完整性。同时，需要提供完善的API接口，支持前端页面的开发和调用。 