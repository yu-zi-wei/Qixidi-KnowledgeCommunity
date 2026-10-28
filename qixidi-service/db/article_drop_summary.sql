-- 删除文章总结字段（article_summary）
-- 背景：文章总结与摘要高度重复，全链路下线总结，摘要统一承载该职责
-- 配套代码：commit 0be2d637（BO/Entity/VO/XML/AI 生成逻辑/前端展示全删）
-- 执行时机：必须先部署新版后端（旧代码查询仍含该列，先删列会报 SQL 错误）
-- 适用环境：本机 qixidi_admin 与生产库均需执行

ALTER TABLE b_article_information DROP COLUMN article_summary;

-- 验证：返回空即删除成功
SHOW COLUMNS FROM b_article_information LIKE 'article_summary';
