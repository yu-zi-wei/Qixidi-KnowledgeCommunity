-- 操作日志 dictum 模块术语校正：名言 → 随笔（2026-10-31）
-- 依据业务术语表（dictum 用户可见名称为"随笔"），代码侧 @LogModule/@Log 已同步替换，
-- 本脚本对齐存量日志的 module 与 title，避免新旧日志在操作日志页混用两套叫法。
-- 幂等：替换后不含"名言"字样，重复执行无影响。
UPDATE sys_oper_log
SET module = REPLACE(module, '名言', '随笔'),
    title  = REPLACE(title, '名言', '随笔')
WHERE module LIKE '%名言%' OR title LIKE '%名言%';
