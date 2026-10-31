-- ============================================================
-- 网站统计表存量 NULL 计数清 0（2026-10-31）
-- 背景：换库发版后首页 /system/stat/data/the/info 报 NPE——
--   StatTask 从不赋值 onlineUserCount/favoritesCount，且 insertUpdate/insertLists
--   的 INSERT VALUES 裸值无 ifnull 兜底（stat_time 唯一键一天只插一次，走不到
--   update 分支的兜底），历史行计数列落 NULL；theList 反射 get().toString() 即炸。
--   代码侧已同步修复（VALUES ifnull 化 + theList 判空），本脚本清存量。
-- 幂等：NULL 已清后重复执行无影响。
-- ============================================================

-- 月统计表（theList 消费方，首页趋势图数据源）
UPDATE b_stat_data SET user_count = 0        WHERE user_count IS NULL;
UPDATE b_stat_data SET onLine_user_count = 0 WHERE onLine_user_count IS NULL;
UPDATE b_stat_data SET article_count = 0     WHERE article_count IS NULL;
UPDATE b_stat_data SET special_count = 0     WHERE special_count IS NULL;
UPDATE b_stat_data SET favorites_count = 0   WHERE favorites_count IS NULL;
UPDATE b_stat_data SET dictum_group_count = 0 WHERE dictum_group_count IS NULL;
UPDATE b_stat_data SET dictum_album_count = 0 WHERE dictum_album_count IS NULL;
UPDATE b_stat_data SET time_notes_count = 0  WHERE time_notes_count IS NULL;
UPDATE b_stat_data SET dictum_count = 0      WHERE dictum_count IS NULL;

-- 日统计表（月表数据来源，同为 NULL 源头）
UPDATE b_stat_data_info SET user_count = 0        WHERE user_count IS NULL;
UPDATE b_stat_data_info SET onLine_user_count = 0 WHERE onLine_user_count IS NULL;
UPDATE b_stat_data_info SET article_count = 0     WHERE article_count IS NULL;
UPDATE b_stat_data_info SET special_count = 0     WHERE special_count IS NULL;
UPDATE b_stat_data_info SET favorites_count = 0   WHERE favorites_count IS NULL;
UPDATE b_stat_data_info SET dictum_group_count = 0 WHERE dictum_group_count IS NULL;
UPDATE b_stat_data_info SET dictum_album_count = 0 WHERE dictum_album_count IS NULL;
UPDATE b_stat_data_info SET time_notes_count = 0  WHERE time_notes_count IS NULL;
UPDATE b_stat_data_info SET dictum_count = 0      WHERE dictum_count IS NULL;
