-- ============================================================
-- sys_config 参数配置表下线（2026-10-31）
-- 背景：参数设置页不再开发（P1-2 取消），sys_config 全链路删除
-- 配置项迁移：
--   sys.account.captchaOnOff → application.yml captcha.enabled（现值 false）
--   sys.account.registerUser → application.yml sys.register-enabled（现值 false）
-- ============================================================

DROP TABLE IF EXISTS sys_config;

-- 旧后台菜单「参数设置」及按钮权限同步清理（幂等）
DELETE FROM sys_menu WHERE menu_name = '参数设置';
DELETE FROM sys_menu WHERE perms LIKE 'system:config%';

-- Redis 残留缓存 sys_config:* 已无读取方，无害；
-- 如需清理只删该前缀 key（本机 Redis 与其他项目共用，勿动 token:*）
