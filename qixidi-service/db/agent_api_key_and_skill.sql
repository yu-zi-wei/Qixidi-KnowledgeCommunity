-- ----------------------------
-- agent API key 表（qixidi-mcp 模块）
-- key 明文仅创建时返回一次，库中只存 SHA-256；Sa-Token 会话的 device 为 "mcp-{id}"，吊销按 device 精确登出
-- ----------------------------
CREATE TABLE `b_agent_api_key` (
  `id`             bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`        varchar(64)  NOT NULL COMMENT '所属用户 uuid（b_user_main 主键）',
  `key_name`       varchar(64)  NOT NULL COMMENT '备注名（如 workbuddy）',
  `key_hash`       char(64)     NOT NULL COMMENT 'key 明文的 SHA-256（小写 hex）',
  `status`         tinyint      NOT NULL DEFAULT 1 COMMENT '状态（1 启用 0 停用，预留；当前吊销即删行）',
  `expire_time`    datetime     DEFAULT NULL COMMENT '过期时间',
  `last_used_time` datetime     DEFAULT NULL COMMENT '最后使用时间（约 5 分钟缓存粒度）',
  `create_by`      varchar(64)  DEFAULT NULL COMMENT '创建者',
  `create_time`    datetime     DEFAULT NULL COMMENT '创建时间',
  `update_by`      varchar(64)  DEFAULT NULL COMMENT '更新者',
  `update_time`    datetime     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_key_hash` (`key_hash`),
  KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'agent 接入 API key';

-- ----------------------------
-- agent skill 表（qixidi-mcp 模块）
-- user_id 为 NULL 表示平台官方 skill（全局分发），所有 agent 可读；用户自建 skill 仅本人可读
-- ----------------------------
CREATE TABLE `b_agent_skill` (
  `id`          bigint       NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id`     varchar(64)  DEFAULT NULL COMMENT '所属用户 uuid（NULL = 平台官方 skill）',
  `name`        varchar(100) NOT NULL COMMENT 'skill 名称',
  `description` varchar(500) DEFAULT NULL COMMENT 'skill 简介（agent 通过 list_skills 判断是否加载）',
  `content`     mediumtext   COMMENT 'skill 正文（Markdown，写作指令）',
  `status`      tinyint      NOT NULL DEFAULT 1 COMMENT '状态（1 启用 0 停用）',
  `create_by`   varchar(64)  DEFAULT NULL COMMENT '创建者',
  `create_time` datetime     DEFAULT NULL COMMENT '创建时间',
  `update_by`   varchar(64)  DEFAULT NULL COMMENT '更新者',
  `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = 'agent 写作 skill（两级：平台官方 + 用户自建）';
