-- sys_oper_log 模块字段（module）迁移脚本
-- 背景：@Log 注解体系升级——类上 @LogModule("模块名") 标识模块，@Log(title) 表达具体操作功能（操作名+对象，如"合并标签信息"）。
-- 执行说明：
--   新环境按 段A → 段B → 段C 顺序全量执行；
--   已执行过 module 提取（v1）的库只需执行段C（段C 按 title 值匹配，天然幂等）。
-- 依赖：MySQL 8.0（REGEXP_REPLACE）。

SET NAMES utf8mb4;

-- ============ 段A：加 module 列（title 后） ============
-- 已执行过的库会报 Duplicate column name 'module'，属预期，跳过即可（MySQL 8.0 不支持 ADD COLUMN IF NOT EXISTS）。
ALTER TABLE sys_oper_log
    ADD COLUMN module VARCHAR(50) DEFAULT NULL COMMENT '操作模块（类上 @LogModule 标识）' AFTER title;

-- ============ 段B：存量迁移 v1（仅新库从原始数据执行一次） ============
-- 旧格式 title 为「模块名（操作详情）」，拆出 module 并把 title 重写为操作名。
-- 幂等条件：仅处理 module 仍为空的行。

-- B1. title → module（括号后缀剥离；动作短语特殊映射到所属模块）
UPDATE sys_oper_log SET module = CASE
    WHEN title LIKE '文章信息（%' THEN '文章信息'
    WHEN title IN ('新增点赞', '取消点赞') THEN '点赞'
    WHEN title IN ('新增关注', '取消关注') THEN '关注'
    WHEN title IN ('添加收藏夹文章', '删除收藏夹文章', '转移收藏夹文章') THEN '收藏夹文章'
    WHEN title IN ('用户签到') THEN '签到'
    WHEN title IN ('发起支付') THEN '支付'
    WHEN title IN ('文章信息审核') THEN '文章信息'
    WHEN title IN ('更新平台用户') THEN '用户信息'
    WHEN title IN ('修改邮箱手机号') THEN '个人信息'
    WHEN title IN ('对象存储配置', '对象存储状态修改') THEN 'OSS对象存储'
    WHEN title IN ('相册回填') THEN '时光相册'
    WHEN title IN ('新增名言信息', '批量新增名言信息', '更新名言信息', '删除名言信息') THEN '名言信息'
    WHEN title IN ('删除专栏信息') THEN '专栏信息'
    WHEN title IN ('新增用户反馈', '更新用户反馈') THEN '用户反馈'
    ELSE REGEXP_REPLACE(title, '（.*）', '')
END
WHERE module IS NULL;

-- B2. title 重写为操作名（按 business_type）；动作语义特殊的白名单行保留原 title
UPDATE sys_oper_log SET title = CASE business_type
    WHEN 1 THEN '新增' WHEN 2 THEN '修改' WHEN 3 THEN '删除' WHEN 4 THEN '授权'
    WHEN 5 THEN '导出' WHEN 6 THEN '导入' WHEN 7 THEN '强退' WHEN 8 THEN '生成代码'
    WHEN 9 THEN '清空' ELSE '其它' END
WHERE module IS NOT NULL
  AND title NOT IN ('新增点赞', '取消点赞', '新增关注', '取消关注', '添加收藏夹文章', '删除收藏夹文章',
                    '转移收藏夹文章', '用户签到', '发起支付', '文章信息审核');

-- ============ 段C：存量迁移 v2（title 拼接对象名，幂等） ============
-- 与代码侧 @Log(title) 终态对齐：title = 操作名 + 对象（对象名与 @LogModule 一致，个别模块取自然对象名）。
-- WHERE 匹配纯操作名，拼接后不再命中，可重复执行。

UPDATE sys_oper_log SET title = CASE
    -- 模块内动作语义特判（与代码侧 @Log(title) 一致）
    WHEN module = 'IP黑名单' AND title = '加黑' THEN '加黑IP'
    WHEN module = 'IP黑名单' AND title = '移出' THEN '移出IP黑名单'
    WHEN module = 'OSS对象存储' AND title = '新增' THEN '上传文件'   -- upload 接口
    WHEN title = '生成代码' THEN '生成代码'                          -- module='代码生成'，拼接重复
    ELSE CONCAT(title, CASE module
        WHEN '参数管理' THEN '参数'   WHEN '角色管理' THEN '角色'
        WHEN '用户管理' THEN '用户'   WHEN '小记管理' THEN '小记'
        WHEN '屏蔽池库' THEN '屏蔽词' WHEN '菜单管理' THEN '菜单'
        WHEN '部门管理' THEN '部门'   WHEN 'OSS对象存储' THEN '文件'
        ELSE module END)
END
WHERE module IS NOT NULL
  AND title IN ('新增', '修改', '删除', '导出', '导入', '授权', '生成代码', '强退', '清空', '审核', '合并', '加黑', '移出');

-- 旧文案统一为终态命名
UPDATE sys_oper_log SET title = '审核文章信息' WHERE title = '文章信息审核';

-- ============ 验证 ============
SELECT module, title, COUNT(*) cnt
FROM sys_oper_log
GROUP BY module, title
ORDER BY cnt DESC
LIMIT 50;
