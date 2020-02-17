    /*初始化数据*/
    DROP TABLE IF EXISTS t_rt_user;/*SkipError*/
    CREATE TABLE t_rt_user
    (
        id                VARCHAR(64) NOT NULL COMMENT '主键',
        name              VARCHAR(32) COMMENT '姓名',
        phone_number      VARCHAR(64) COMMENT '手机号',
        wechat_code       VARCHAR(64) COMMENT '微信号',
        wechat_id         VARCHAR(1024) COMMENT '微信主键',
        wechat_nick_name  VARCHAR(64) COMMENT '微信参数姓名',
        wechat_avatar_url VARCHAR(1024) COMMENT '微信参数头像地址',
        wechat_gender     INT                  DEFAULT 0 COMMENT '微信参数性别 0：未知、1：男、2：女',
        wechat_city       VARCHAR(64) COMMENT '微信参数城市',
        wechat_province   VARCHAR(64) COMMENT '微信参数省份',
        wechat_country    VARCHAR(64) COMMENT '微信参数国家',
        user_type         INT                  DEFAULT 0 COMMENT '用户类型 0村名，1村管，2系统管理员，3维修人员，4司机',
        approval_status   INT                  DEFAULT 0 COMMENT '审批状态 0待审批，1审批通过，2审批未过',
        work_status       INT                  DEFAULT 0 COMMENT '工作状态 0在职，1离职',
        village_code      VARCHAR(32) COMMENT '所属村居编码',
        town_code         VARCHAR(32) COMMENT '所属乡镇编码',
        address           TEXT COMMENT '详细地址',
        remark            TEXT COMMENT '备注',
        car_no            VARCHAR(64) COMMENT '车牌号',
        create_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        creator           VARCHAR(64) COMMENT '创建人',
        update_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
        delete_flag       INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
        PRIMARY KEY (id)
    ) COMMENT = '用户表 用户表';

    ALTER TABLE t_rt_user
        COMMENT '用户表';
    DROP TABLE IF EXISTS t_rt_organization;/*SkipError*/
    CREATE TABLE t_rt_organization
    (
        id          VARCHAR(64) NOT NULL COMMENT '主键',
        name        VARCHAR(64) COMMENT '组织名称',
        code        VARCHAR(64) COMMENT '编码',
        parent_id   VARCHAR(64) COMMENT '父主键',
        parent_code VARCHAR(64) COMMENT '父编码',
        type        INT                  DEFAULT 0 COMMENT '类型 0乡镇，1村居',
        remark      TEXT COMMENT '备注',
        sort        INT                  DEFAULT 99 COMMENT '排序',
        create_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        creator     VARCHAR(64) COMMENT '创建人',
        update_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
        delete_flag INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
        PRIMARY KEY (id)
    ) COMMENT = '单位表 单位表';

    ALTER TABLE t_rt_organization
        COMMENT '单位表';
    DROP TABLE IF EXISTS t_rt_record_repair;/*SkipError*/
    CREATE TABLE t_rt_record_repair
    (
        id                VARCHAR(64) NOT NULL COMMENT '主键',
        submit_user_id    VARCHAR(64) NOT NULL COMMENT '报修人主键',
        target_user_id    VARCHAR(64) NOT NULL COMMENT '待修人主键',
        operation_user_id VARCHAR(64) COMMENT '维修人主键',
        pump_car_id       VARCHAR(64) COMMENT '冗余字段1',
        report_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
        repair_time       DATETIME COMMENT '上门时间',
        finish_time       DATETIME COMMENT '完成时间',
        repair_status     INT                  DEFAULT 0 COMMENT '维修状态 0未上门，1维修中，2已维修',
        problem           TEXT COMMENT '问题描述',
        remark            TEXT COMMENT '备注',
        type              INT                  DEFAULT 0 COMMENT '类型 0报修，1报抽',
        overtime_flag     INT                  DEFAULT 0 COMMENT '是否超时 0未超时，1已超时',
        create_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        creator           VARCHAR(64) COMMENT '创建人',
        update_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
        delete_flag       INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
        PRIMARY KEY (id)
    ) COMMENT = '报修记录表 ';

    ALTER TABLE t_rt_record_repair
        COMMENT '报修记录表';
    DROP TABLE IF EXISTS t_rt_record_pump;/*SkipError*/
    CREATE TABLE t_rt_record_pump
    (
        id                VARCHAR(64) NOT NULL COMMENT '主键',
        submit_user_id    VARCHAR(64) NOT NULL COMMENT '报抽人主键',
        target_user_id    VARCHAR(64) NOT NULL COMMENT '待抽人主键',
        operation_user_id VARCHAR(64) COMMENT '抽取司机主键',
        pump_car_id       VARCHAR(64) COMMENT '抽取车主键',
        report_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报修时间',
        repair_status     INT                  DEFAULT 0 COMMENT '冗余字段1 0未上门，1维修中，2已维修',
        finish_time       DATETIME COMMENT '完成时间',
        pump_status       INT                  DEFAULT 0 COMMENT '状态 0未抽，1已抽',
        problem           TEXT COMMENT '问题描述',
        remark            TEXT COMMENT '备注',
        type              INT                  DEFAULT 1 COMMENT '类型 0报修，1报抽',
        overtime_flag     INT                  DEFAULT 0 COMMENT '是否超时 0未超时，1已超时',
        create_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        creator           VARCHAR(64) COMMENT '创建人',
        update_time       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
        delete_flag       INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
        PRIMARY KEY (id)
    ) COMMENT = '报抽记录表 ';

    ALTER TABLE t_rt_record_pump
        COMMENT '报抽记录表';
    DROP TABLE IF EXISTS t_rt_cars;/*SkipError*/
    CREATE TABLE t_rt_cars
    (
        id           VARCHAR(64) NOT NULL COMMENT '主键',
        car_no       VARCHAR(64) NOT NULL COMMENT '车牌号',
        village_code VARCHAR(32) COMMENT '所属村居编码',
        town_code    VARCHAR(32) COMMENT '所属乡镇编码',
        duty_user_id VARCHAR(64) COMMENT '当值司机',
        create_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        creator      VARCHAR(64) COMMENT '创建人',
        update_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
        delete_flag  INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
        PRIMARY KEY (id)
    ) COMMENT = '车辆表 ';

    ALTER TABLE t_rt_cars
        COMMENT '车辆表';
    DROP TABLE IF EXISTS t_rt_config;/*SkipError*/
    CREATE TABLE t_rt_config
    (
        id           VARCHAR(64) NOT NULL COMMENT '主键',
        config_name  VARCHAR(64) COMMENT '配置名称',
        config_code  VARCHAR(64) COMMENT '配置编码',
        config_param TEXT COMMENT '配置参数',
        create_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        creator      VARCHAR(64) COMMENT '创建人',
        update_time  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
        delete_flag  INT         NOT NULL DEFAULT 0 COMMENT '删除标记 0未删除(默认)，1删除',
        PRIMARY KEY (id)
    ) COMMENT = '配置表 ';

    ALTER TABLE t_rt_config
        COMMENT '配置表';
