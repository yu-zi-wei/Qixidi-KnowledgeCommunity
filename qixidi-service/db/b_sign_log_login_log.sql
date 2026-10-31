-- b_sign_log 前台登录日志表接入（2026-10-31）
-- 表为预留建表（0 行、代码从未写入），接入前修正两处列类型：
--   1. uid varbinary(64) → varchar(64)：存的是字符串 uuid，varbinary 读写需字节互转
--   2. ip_address varchar(20) → varchar(50)：IPv6 文本最长 45，20 位插入即截断/报错
-- 幂等：类型已是目标类型时重复执行无影响
ALTER TABLE b_sign_log
    MODIFY COLUMN uid varchar(64) NOT NULL COMMENT '用户id',
    MODIFY COLUMN ip_address varchar(50) NOT NULL COMMENT 'ip地址';
