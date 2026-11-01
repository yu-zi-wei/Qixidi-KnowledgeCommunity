-- 工具目录（is_parent=1）历史数据订正：tool_url 曾以占位 'xx' 填充（BO 时代必填约束的产物）
-- 目录在前台只展示 toolName（/white/configure/tool/list → selectListSlq），tool_url 无消费
-- 2026-11-01 起 BO 取消目录必填、Service 层目录写入固定空串，本脚本将存量占位值对齐为 ''
-- 幂等：重复执行结果一致
UPDATE to_tool_info
SET tool_url = ''
WHERE is_parent = 1
  AND tool_url IS NOT NULL
  AND tool_url <> '';
