-- 废弃 sys_oss 表（2026-10-31）
-- 背景：唯一写入点（SysOssServiceImpl.upload 的 baseMapper.insert）早已注释停写，
--       表不再增长且无任何业务以 ossId 反查（消费方只取最终 url），文件本体在 MinIO/OSS 云端不受影响。
-- 同步变更：SysOssController 删除 list/download/remove 管理接口（仅保留 /system/oss/upload 上传通道），
--           admin 前端删除「OSS 文件」菜单与页面。
-- 幂等性：DROP IF EXISTS，重复执行无副作用。
DROP TABLE IF EXISTS sys_oss;
