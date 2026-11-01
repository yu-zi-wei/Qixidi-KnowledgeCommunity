-- 系统消息已读记录表：Redis USER_SYSTEM_MESSAGES:{uid} 的落库兜底
-- 背景：系统消息未读数 = 全部生效消息 id 集 - Redis 已读集（差集），
--   Redis 数据丢失会导致全员公告已读状态重置；本表在用户标记已读时同步落库，
--   listSums 发现 Redis 已读集为空时从本表回灌（库为权威源，Redis 为缓存）。
-- 幂等：CREATE TABLE IF NOT EXISTS，可重复执行。
CREATE TABLE IF NOT EXISTS b_news_system_read (
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    uid         VARCHAR(64) NOT NULL COMMENT '用户id（b_tripartite_user.uuid）',
    news_id     BIGINT      NOT NULL COMMENT '系统消息id（b_news_system_info.id）',
    create_time DATETIME    DEFAULT NULL COMMENT '已读时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_uid_news (uid, news_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '系统消息已读记录';
