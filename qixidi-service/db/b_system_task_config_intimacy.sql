-- 系统任务配置：新增「重算文章标签亲密度任务」（ArticleIntimacyTask，ArticleInformationServiceImpl 的精选推荐数据源）
-- 增量分只累加不衰减，本任务按 DB 全量行为重建 Redis zset（浏览+1/点赞+2/收藏+3 按半衰期 30 天衰减，标签关注 +5 不衰减）
-- 幂等：id=11 行不存在时才插入；Mapper.addExecutionSum 按 id update，无此行则执行计数静默丢失
INSERT INTO b_system_task_config (id, task_name, task_execution_time, task_execution_sum, task_url)
SELECT 11, '重算文章标签亲密度任务', '每天 04:30 一次', 0, '/system/task/recalculate_article_intimacy'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM b_system_task_config WHERE id = 11);
