-- 废弃表清理：b_news_user_info（用户消息旧表）
-- 背景：前台消息中心 2023-07 起全面切换 b_news_user_record，本表最后一次写入 2023-06-25，此后零读写；
--       2026-11-01 代码侧删除全部引用（实体/BO/Mapper/XML/后台旧 CRUD/前台 add+remove 死接口）后执行本脚本。
-- 幂等：IF EXISTS，重复执行无二次影响。

DROP TABLE IF EXISTS `b_news_user_info`;
