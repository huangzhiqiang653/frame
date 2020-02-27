/*初始化脚本*/
/*初始化数据库*/
CREATE DATABASE IF NOT EXISTS zx_frame_db default charset utf8 COLLATE utf8_general_ci;
use zx_frame_db;
/*初始化表*/
DROP TABLE if exists auth_zx_user;/*SkipError*/
CREATE TABLE auth_zx_user
(
    id              VARCHAR(64) NOT NULL COMMENT '主键',
    user_name       VARCHAR(64) COMMENT '姓名',
    sex             INT                  DEFAULT 2 COMMENT '性别 0女，1男，2未知(默认)',
    birth_day       DATETIME COMMENT '出生年月日',
    head_url        VARCHAR(128) COMMENT '头像地址',
    head_blod       TEXT COMMENT '头像文件',
    organization_id VARCHAR(64) COMMENT '所属机构',
    email           VARCHAR(128) COMMENT '邮箱',
    phone_number    VARCHAR(64) COMMENT '手机号码',
    status          INT                  DEFAULT 0 COMMENT '用户状态 0启用(默认)，1禁用',
    remark          TEXT COMMENT '备注',
    create_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator         VARCHAR(64) COMMENT '创建人',
    update_time     DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag     INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '用户表';

ALTER TABLE auth_zx_user
    COMMENT '用户表';
DROP TABLE if exists auth_zx_account;/*SkipError*/
CREATE TABLE auth_zx_account
(
    id               VARCHAR(64) NOT NULL COMMENT '主键',
    user_id VARCHAR(64)    COMMENT '用户主键' ,
    account_name     VARCHAR(64) COMMENT '账户名称',
    account_password VARCHAR(64) COMMENT '账户密码',
    status           INT                  DEFAULT 0 COMMENT '账号状态 0启用(默认)，1禁用',
    sort             INT                  DEFAULT 99 COMMENT '排序',
    create_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator          VARCHAR(64) COMMENT '创建人',
    update_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag      INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '账户表 ';

ALTER TABLE auth_zx_account
    COMMENT '账户表';
DROP TABLE if exists auth_zx_resource;/*SkipError*/
CREATE TABLE auth_zx_resource
(
    id            VARCHAR(64) NOT NULL COMMENT '主键',
    relation_id   VARCHAR(64) COMMENT '关联主键',
    resource_name VARCHAR(32) COMMENT '资源名称',
    resource_code VARCHAR(64) COMMENT '资源编码',
    remark        TEXT COMMENT '备注',
    sort          INT         NOT NULL DEFAULT 99 COMMENT '排序',
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator       VARCHAR(64) COMMENT '创建人',
    update_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag   INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '资源表 ';

ALTER TABLE auth_zx_resource
    COMMENT '资源表';
DROP TABLE if exists auth_zx_menu;/*SkipError*/
CREATE TABLE auth_zx_menu
(
    id          VARCHAR(64) NOT NULL COMMENT '主键',
    parent_id   VARCHAR(64) COMMENT '父主键',
    level       INT                  DEFAULT 1 COMMENT '层级',
    name        VARCHAR(32) COMMENT '名称',
    code        VARCHAR(64) COMMENT '编码',
    menu_type   VARCHAR(64)          DEFAULT 0 COMMENT '类型 0一级跳转(默认)，1二级跳转',
    icon        VARCHAR(64) COMMENT '图标',
    remark      TEXT COMMENT '备注',
    url         VARCHAR(128) COMMENT '地址',
    sort        INT                  DEFAULT 99 COMMENT '排序',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator     VARCHAR(64) COMMENT '创建人',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '菜单表 ';

ALTER TABLE auth_zx_menu
    COMMENT '菜单表';
DROP TABLE if exists auth_zx_organization;/*SkipError*/
CREATE TABLE auth_zx_organization
(
    id          VARCHAR(64) NOT NULL COMMENT '主键',
    full_name   VARCHAR(128) COMMENT '名称全程',
    short_name  VARCHAR(64) COMMENT '简称',
    code        VARCHAR(64) COMMENT '编码',
    type        VARCHAR(64) COMMENT '组织类型',
    parent_id   VARCHAR(64) COMMENT '父主键',
    remark      TEXT COMMENT '备注',
    sort        INT                  DEFAULT 99 COMMENT '排序',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator     VARCHAR(64) COMMENT '创建人',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '组织表 ';

ALTER TABLE auth_zx_organization
    COMMENT '组织表';
DROP TABLE if exists auth_zx_role;/*SkipError*/
CREATE TABLE auth_zx_role
(
    id          VARCHAR(64) NOT NULL COMMENT '主键',
    name        VARCHAR(64) COMMENT '角色名称',
    code        VARCHAR(32) COMMENT '角色编码',
    remark      TEXT COMMENT '备注',
    sort        INT                  DEFAULT 99 COMMENT '排序',
    create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator     VARCHAR(64) COMMENT '创建人',
    update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '角色表 ';

ALTER TABLE auth_zx_role
    COMMENT '角色表';
DROP TABLE if exists auth_zx_relation_role_menu;/*SkipError*/
CREATE TABLE auth_zx_relation_role_menu
(
    id      VARCHAR(64) NOT NULL COMMENT '主键',
    role_id VARCHAR(64) COMMENT '角色主键',
    menu_id VARCHAR(64) COMMENT '菜单主键',
    PRIMARY KEY (id)
) COMMENT = '角色菜单关联表 ';

ALTER TABLE auth_zx_relation_role_menu
    COMMENT '角色菜单关联表';
DROP TABLE if exists auth_zx_relation_role_resource;/*SkipError*/
CREATE TABLE auth_zx_relation_role_resource
(
    id          VARCHAR(64) NOT NULL COMMENT '主键',
    role_id     VARCHAR(64) COMMENT '角色主键',
    menu_id     VARCHAR(64) COMMENT '菜单主键',
    resource_id VARCHAR(64) COMMENT '资源主键',
    PRIMARY KEY (id)
) COMMENT = '角色资源关联表 ';

ALTER TABLE auth_zx_relation_role_resource
    COMMENT '角色资源关联表';
DROP TABLE if exists auth_zx_relaiton_account_role;/*SkipError*/
CREATE TABLE auth_zx_relaiton_account_role
(
    id         VARCHAR(64) NOT NULL COMMENT '主键',
    account_id VARCHAR(64) COMMENT '账号主键',
    role_id    VARCHAR(64) COMMENT '角色主键',
    PRIMARY KEY (id)
) COMMENT = '账号角色关联表 ';

ALTER TABLE auth_zx_relaiton_account_role
    COMMENT '账号角色关联表';
DROP TABLE if exists auth_zx_relation_user_account;/*SkipError*/
CREATE TABLE auth_zx_relation_user_account
(
    id         VARCHAR(64) NOT NULL COMMENT '主键',
    user_id    VARCHAR(64) COMMENT '用户主键',
    account_id VARCHAR(64) COMMENT '账户主键',
    PRIMARY KEY (id)
) COMMENT = '用户角色关联表 ';

ALTER TABLE auth_zx_relation_user_account
    COMMENT '用户角色关联表';
DROP TABLE if exists auth_zx_log;/*SkipError*/
CREATE TABLE auth_zx_log
(
    id            VARCHAR(64) NOT NULL COMMENT '主键',
    user_id       VARCHAR(64) COMMENT '操作人主键',
    request_url   VARCHAR(128) COMMENT '请求地址',
    request_type  VARCHAR(32) COMMENT '请求类型 GET、POST、DELETE、PUT等',
    menu_id       VARCHAR(64) COMMENT '菜单主键',
    resource_id   VARCHAR(64) COMMENT '资源主键',
    request_data  TEXT COMMENT '请求数据',
    response_data TEXT COMMENT '返回数据',
    PRIMARY KEY (id)
) COMMENT = '系统日志表 ';

ALTER TABLE auth_zx_log
    COMMENT '系统日志表';
DROP TABLE if exists auth_zx_dictionary;/*SkipError*/
CREATE TABLE auth_zx_dictionary
(
    id          VARCHAR(64) NOT NULL COMMENT '主键',
    name        VARCHAR(64) COMMENT '名称',
    code        VARCHAR(64) COMMENT '编码',
    type        VARCHAR(32) COMMENT '类型',
    sort        INT DEFAULT 1 COMMENT '排序',
    parent_id   VARCHAR(64) COMMENT '父主键',
    parent_code VARCHAR(64) COMMENT '父编码',
    PRIMARY KEY (id)
) COMMENT = '字典表 ';

ALTER TABLE auth_zx_dictionary
    COMMENT '字典表';
DROP TABLE if exists auth_zx_data_extend;/*SkipError*/
CREATE TABLE auth_zx_data_extend
(
    id                VARCHAR(64) NOT NULL COMMENT '主键',
    system_table_name VARCHAR(32) COMMENT '表名',
    data_id           VARCHAR(64) COMMENT '数据主键',
    field_name        VARCHAR(64) COMMENT '字段名称',
    field_type        VARCHAR(64) COMMENT '字段类型',
    field_value       TEXT COMMENT '字段值',
    create_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator           VARCHAR(64) COMMENT '创建人',
    update_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag       INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '数据扩展表 ';

ALTER TABLE auth_zx_data_extend
    COMMENT '数据扩展表';
DROP TABLE if exists auth_zx_task;/*SkipError*/
CREATE TABLE auth_zx_task
(
    id            VARCHAR(64) NOT NULL COMMENT '主键',
    task_name     VARCHAR(64) COMMENT '任务名称',
    target_class  VARCHAR(64) COMMENT '目标类',
    target_method VARCHAR(64) COMMENT '目标方法',
    execute_time  DATETIME COMMENT '执行时间',
    time_regular  VARCHAR(64) COMMENT '时间正则',
    exeute_rule   INT                  DEFAULT 0 COMMENT '执行规则 0定时执行(默认)，1执行一次',
    status        INT                  DEFAULT 0 COMMENT '任务状态 0待执行(默认)，1执行中，2已执行',
    remark        TEXT COMMENT '备注',
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    creator       VARCHAR(64) COMMENT '创建人',
    update_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    delete_flag   INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
    PRIMARY KEY (id)
) COMMENT = '定时任务 ';

ALTER TABLE auth_zx_task
    COMMENT '定时任务';
DROP TABLE if exists auth_zx_task_log;/*SkipError*/
CREATE TABLE auth_zx_task_log
(
    id             VARCHAR(64) NOT NULL COMMENT '主键',
    task_id        VARCHAR(64) COMMENT '任务主键',
    execute_time   DATETIME COMMENT '执行时间',
    execute_result INT DEFAULT 0 COMMENT '执行结果 0成功(默认)，1失败，2未知',
    data           TEXT COMMENT '相关数据',
    PRIMARY KEY (id)
) COMMENT = '任务日志记录表 ';

ALTER TABLE auth_zx_task_log
    COMMENT '任务日志记录表';
