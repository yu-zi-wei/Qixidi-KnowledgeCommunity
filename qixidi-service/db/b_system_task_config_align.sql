-- 系统任务配置表（b_system_task_config）对齐实际定时任务
-- 1) 新增"最新执行时间"列：Mapper.addExecutionSum 执行成功时与次数一并刷新
-- 2) cron 描述对齐：DictumTask 实际 0 */12（每12分钟）、LabelTask.syncLabel 实际 0 */21（每21分钟），表内文案仍是旧值
-- 3) 补缺失任务：LabelTask.syncLabelGroup（0 */47 * * * ?，每47分钟）此前无配置行、无执行计数、无手动端点
-- 幂等性：ALTER 重复执行报列已存在（注释列不存在时才执行）；UPDATE/INSERT 前 10 号行不存在时才插入
ALTER TABLE b_system_task_config
    ADD COLUMN task_last_execution_time datetime DEFAULT NULL COMMENT '最新执行时间（定时/手动执行成功时刷新）' AFTER task_execution_sum;

UPDATE b_system_task_config SET task_execution_time = '每12分钟一次' WHERE id = 4 AND task_execution_time <> '每12分钟一次';
UPDATE b_system_task_config SET task_execution_time = '每21分钟一次' WHERE id = 5 AND task_execution_time <> '每21分钟一次';

INSERT INTO b_system_task_config (id, task_name, task_execution_time, task_execution_sum, task_url)
SELECT 10, '同步标签分组数据任务', '每47分钟一次', 0, '/system/task/synchronize_label_group_data'
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM b_system_task_config WHERE id = 10);
