-- ============================================================
-- RBAC 简化改造（2026-10-30 定版）
-- 模型：sys_user → sys_user_role → sys_role → sys_role_perm(权限码)
-- 权限码字典不落库：后端启动扫描 @SaCheckPermission 自动注册
-- 菜单不落库：前端代码维护，按权限码推导可见性
-- 本脚本批次 1 部分在新体系上线时执行；批次 2 的 DROP 在遗留代码删除后执行
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

-- ---------- 批次 2：遗留删除（遗留代码删除后执行） ----------

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

-- sys_user 删部门遗留列（LoginUser/SysUser 实体同步瘦身）
ALTER TABLE sys_user DROP COLUMN dept_id;
