-- ============================================================
-- sys 遗留模块归档脚本（2026-10-31，M3 收尾）
-- 范围：字典 + 通知 三张表（其余 RBAC 表已由 rbac-simplify.sql 处理）
-- 对应代码删除：SysDictType/SysDictData/SysNotice 全模块
--   （唯一消费方是已归档的旧后台 1.0；新后台 admin 2.0 与门户均零调用）
--
-- 方式：RENAME 成 _deprecated_ 前缀而非直接 DROP——观察一个版本无回滚需求后清理
--
-- 🔴 执行前提：
--   1. 新版后端 jar 已部署（删除字典/通知代码的版本）
--      ——旧版代码启动期 loadingDictCache 会直查 sys_dict_type，先执行脚本旧服务起不来
--   2. 生产执行前先备份：
--      mysqldump -u<user> -p <库名> sys_dict_type sys_dict_data sys_notice > dict-notice-backup.sql
--
-- 回滚：执行下方注释掉的逆操作 RENAME 回原名即可
-- ============================================================

RENAME TABLE sys_dict_type TO _deprecated_sys_dict_type;
RENAME TABLE sys_dict_data TO _deprecated_sys_dict_data;
RENAME TABLE sys_notice    TO _deprecated_sys_notice;

-- ---------- 回滚（如需）----------
-- RENAME TABLE _deprecated_sys_dict_type TO sys_dict_type;
-- RENAME TABLE _deprecated_sys_dict_data TO sys_dict_data;
-- RENAME TABLE _deprecated_sys_notice    TO sys_notice;

-- ---------- 观察期结束后的清理（另行执行）----------
-- DROP TABLE IF EXISTS _deprecated_sys_dict_type;
-- DROP TABLE IF EXISTS _deprecated_sys_dict_data;
-- DROP TABLE IF EXISTS _deprecated_sys_notice;
