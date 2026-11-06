-- 启航的班级管理系统：数据库结构
-- 适用于 MySQL 8.0+
CREATE DATABASE IF NOT EXISTS `start`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_0900_ai_ci;
USE `start`;

CREATE TABLE IF NOT EXISTS `dept` (
    `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID, 主键',
    `name`        VARCHAR(10)  NOT NULL COMMENT '部门名称',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '部门表';

CREATE TABLE IF NOT EXISTS `emp` (
    `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID, 主键',
    `username`    VARCHAR(20)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(100) DEFAULT NULL COMMENT '密码，支持 BCrypt；历史明文数据兼容',
    `name`        VARCHAR(10)  NOT NULL COMMENT '姓名',
    `gender`      TINYINT UNSIGNED NOT NULL COMMENT '性别, 1:男, 2:女',
    `phone`       CHAR(11)     NOT NULL COMMENT '手机号',
    `job`         TINYINT UNSIGNED DEFAULT NULL COMMENT '职位, 1:班主任, 2:讲师, 3:学工主管, 4:教研主管, 5:咨询师',
    `salary`      INT UNSIGNED DEFAULT NULL COMMENT '薪资',
    `image`       VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `entry_date`  DATE         DEFAULT NULL COMMENT '入职日期',
    `dept_id`     INT UNSIGNED DEFAULT NULL COMMENT '部门ID',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`),
    UNIQUE KEY `phone` (`phone`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '员工表';

CREATE TABLE IF NOT EXISTS `emp_expr` (
    `id`      INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID, 主键',
    `emp_id`  INT UNSIGNED DEFAULT NULL COMMENT '员工ID',
    `begin`   DATE         DEFAULT NULL COMMENT '开始时间',
    `end`     DATE         DEFAULT NULL COMMENT '结束时间',
    `company` VARCHAR(50)  DEFAULT NULL COMMENT '公司名称',
    `job`     VARCHAR(50)  DEFAULT NULL COMMENT '职位',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '工作经历';

CREATE TABLE IF NOT EXISTS `clazz` (
    `id`          INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID, 主键',
    `name`        VARCHAR(30)  NOT NULL COMMENT '班级名称',
    `room`        VARCHAR(20)  DEFAULT NULL COMMENT '班级教室',
    `begin_date`  DATE         NOT NULL COMMENT '开课时间',
    `end_date`    DATE         NOT NULL COMMENT '结课时间',
    `master_id`   INT UNSIGNED DEFAULT NULL COMMENT '班主任ID，关联员工表ID',
    `subject`     TINYINT UNSIGNED NOT NULL COMMENT '学科, 1:Java, 2:前端, 3:大数据, 4:Python, 5:Go, 6:嵌入式',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time` DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '班级表';

CREATE TABLE IF NOT EXISTS `student` (
    `id`              INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID, 主键',
    `name`            VARCHAR(10)  NOT NULL COMMENT '姓名',
    `no`              CHAR(10)     NOT NULL COMMENT '学号',
    `gender`          TINYINT UNSIGNED NOT NULL COMMENT '性别, 1:男, 2:女',
    `phone`           VARCHAR(11)  NOT NULL COMMENT '手机号',
    `id_card`         CHAR(18)     NOT NULL COMMENT '身份证号',
    `is_college`      TINYINT UNSIGNED NOT NULL COMMENT '是否来自院校, 1:是, 0:否',
    `address`         VARCHAR(100) DEFAULT NULL COMMENT '联系地址',
    `degree`          TINYINT UNSIGNED DEFAULT NULL COMMENT '最高学历, 1:初中, 2:高中, 3:大专, 4:本科, 5:硕士, 6:博士',
    `graduation_date` DATE         DEFAULT NULL COMMENT '毕业时间',
    `clazz_id`        INT UNSIGNED NOT NULL COMMENT '班级ID',
    `violation_count` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '违纪次数',
    `violation_score` TINYINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '违纪扣分',
    `create_time`     DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_time`     DATETIME     DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `no` (`no`),
    UNIQUE KEY `phone` (`phone`),
    UNIQUE KEY `id_card` (`id_card`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '学员表';

CREATE TABLE IF NOT EXISTS `user` (
    `id`          INT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(50)  NOT NULL COMMENT '登录账号',
    `password`    VARCHAR(100) NOT NULL COMMENT 'BCrypt 密文',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '显示名',
    `role`        VARCHAR(20)  DEFAULT 'admin' COMMENT '角色',
    `status`      TINYINT      DEFAULT 1 COMMENT '1 启用，0 禁用',
    `create_time` DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_username` (`username`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '系统用户表';