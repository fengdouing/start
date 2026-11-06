-- 初始化本地演示账号 admin（BCrypt 密文）；首次登录后请立即修改密码。
-- 首次运行会插入账号；重复运行不会覆盖已修改的密码。
USE `start`;

INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `status`)
VALUES ('admin', '$2a$10$R66oXW2Ie8spPFXnlrPAZeAVK1VBUoCCw4TmIvCFRW5ZmERpYqY4K', '管理员', 'admin', 1)
ON DUPLICATE KEY UPDATE
    `nickname` = VALUES(`nickname`),
    `role` = VALUES(`role`),
    `status` = VALUES(`status`);