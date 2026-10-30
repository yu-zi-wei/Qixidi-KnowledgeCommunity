-- ============================================================
-- RBAC 简化改造 · 生产迁移脚本（2026-10-30 定版，dev 已验证）
-- 模型：sys_user → sys_user_role → sys_role → sys_role_perm(权限码)
-- 权限码字典不落库：后端启动扫描 @SaCheckPermission 自动注册
-- 菜单不落库：前端代码维护，按权限码推导可见性
--
-- 🔴 执行顺序（必须）：
--   1. 停旧版后端服务
--   2. 备份将被删除的表（DROP 不可逆）：
--      mysqldump -u<user> -p <库名> sys_menu sys_role_menu sys_dept sys_post sys_role_dept sys_user_post sys_role sys_user > rbac-backup.sql
--   3. 执行本脚本（批次 1 + 批次 2 一口气跑完）
--   4. 部署新版后端 jar 并启动
--   ⚠️ 顺序不能反：旧版代码 join sys_dept/sys_post，先 DROP 表旧服务登录链路直接 500
--
-- 🔴 删角色警告：
--   DELETE sys_role WHERE role_id <> 1 会删掉除超管外的全部角色（含自建角色）。
--   dev 库已确认只剩 RuoYi 原生角色；生产执行前先
--   SELECT role_id, role_name FROM sys_role WHERE role_id <> 1;
--   确认没有要保留的自建角色，有则先把它的 role_id 补进下方保留名单。
-- ============================================================

-- ---------- 批次 1：新体系 ----------

-- 角色权限码关联表
CREATE TABLE IF NOT EXISTS sys_role_perm (
    role_id BIGINT       NOT NULL COMMENT '角色ID',
    perm    VARCHAR(100) NOT NULL COMMENT '权限码（模块:实体:动作）',
    PRIMARY KEY (role_id, perm)
) ENGINE = InnoDB COMMENT = '角色与权限码关联';

-- 清理 RuoYi 原生角色与关联（保留 admin=1，超管权限走代码通配不落表）
DELETE FROM sys_user_role WHERE role_id <> 1;
DELETE FROM sys_role WHERE role_id <> 1;

-- ---------- 批次 2：遗留删除 ----------

-- RBAC 装置 6 张遗留表
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_dept;
DROP TABLE IF EXISTS sys_post;
DROP TABLE IF EXISTS sys_role_dept;
DROP TABLE IF EXISTS sys_user_post;

-- sys_role 删 RuoYi 专属列
ALTER TABLE sys_role
    DROP COLUMN data_scope,
    DROP COLUMN menu_check_strictly,
    DROP COLUMN dept_check_strictly;

-- sys_user 删部门遗留列（SysUser 实体同步瘦身）
ALTER TABLE sys_user DROP COLUMN dept_id;
