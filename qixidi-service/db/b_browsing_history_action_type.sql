-- ============================================================
-- 浏览历史表加行为类型：区分足迹是 阅读 / 点赞 / 评论 产生
-- 2026-11-23 与应用 v2.2 一同发布
-- 幂等：重复执行报 Duplicate column 即已完成
-- ============================================================

ALTER TABLE b_browsing_history
    ADD COLUMN action_type tinyint NOT NULL DEFAULT 1 COMMENT '行为类型（1：阅读，2：点赞，3：评论）' AFTER target_type;
