-- ============================================================
-- 清理点赞表重复记录：旧版点赞判断失效 + 无防重导致同一用户对同一内容
-- 累计插入多条记录（点赞数虚高）；v2.1.1 起取消赞会一次删光全部匹配行，
-- 残留重复会在用户下一次取消时表现为"点赞数骤降"。本脚本提前清账：
-- 每个 (uid + type + type_id) 只保留最新一条（id 最大）
-- 幂等：重复执行无副作用（清完后 HAVING >1 为空集）
-- 2026-11-23 v2.1.1 配套脚本
-- ============================================================

DELETE r FROM b_fabulous_record r
JOIN (
    SELECT type_id, type, uid, MAX(id) AS keep_id
    FROM b_fabulous_record
    GROUP BY type_id, type, uid
    HAVING COUNT(*) > 1
) k ON r.type_id = k.type_id
   AND r.type = k.type
   AND r.uid = k.uid
   AND r.id < k.keep_id;
