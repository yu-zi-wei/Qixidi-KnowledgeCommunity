-- ----------------------------
-- 时光小记评论表（结构对齐 b_dictum_comment）
-- @date 2026-09-15
-- ----------------------------
CREATE TABLE `b_time_notes_comment` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'id',
  `time_notes_id` BIGINT       NOT NULL COMMENT '时光小记id',
  `uid`           VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '小记用户id',
  `parent_id`     BIGINT       NOT NULL DEFAULT 0 COMMENT '父级评论id（一级评论 = 小记id）',
  `comment_grade` TINYINT      NOT NULL DEFAULT 1 COMMENT '评论等级（1：一级，2：二级，3：三级及以下）',
  `target_id`     BIGINT       DEFAULT NULL COMMENT '目标id（被回复的评论id或小记id）',
  `target_uid`    VARCHAR(64)  DEFAULT NULL COMMENT '目标用户id',
  `comment_uid`   VARCHAR(64)  NOT NULL COMMENT '评论人id',
  `content`       VARCHAR(1000) NOT NULL COMMENT '评论内容',
  `type`          TINYINT      NOT NULL DEFAULT 1 COMMENT '评论类型（1：小记，2：评论）',
  `status`        TINYINT      NOT NULL DEFAULT 0 COMMENT '评论状态（0：正常，1：已删除）',
  `create_time`   DATETIME     DEFAULT NULL COMMENT '创建时间',
  `update_time`   DATETIME     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_time_notes_id` (`time_notes_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_comment_uid` (`comment_uid`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_cs_0900_ai_ci COMMENT = '时光小记评论表';
-- collation 必须与库内老表一致（utf8mb4_cs_0900_ai_ci）：跨表 join 字符串比较时
-- 若与老表（如 b_user_main）不一致会报 Illegal mix of collations（2026-09-15 踩坑）
