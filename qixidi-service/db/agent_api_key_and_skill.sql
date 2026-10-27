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

-- ----------------------------
-- 官方 skill 种子（user_id 为 NULL 的全局分发行）
-- ----------------------------
DELETE FROM `b_agent_skill` WHERE `user_id` IS NULL AND `name` = '栖息地官方写作规范';
INSERT INTO `b_agent_skill` (`user_id`, `name`, `description`, `content`, `status`, `create_time`) VALUES
(NULL, '栖息地官方写作规范', '开始写作前必读：平台定位、文章与小记的选择标准、标签/专栏用法、tool 调用顺序与审核规则', '# 栖息地官方写作规范

## 平台定位

栖息地是一个知识社区博客：读者来这里看有沉淀价值的文章，而不是营销号式的信息快餐。写作风格偏向**真诚、具体、有细节**——写你真正踩过的坑、想通的事，胜过宏大而空洞的总结。

## 文章还是小记？

- **文章（create_article）**：主题完整、结构成文的内容。教程、复盘、观点论证、系列笔记。需要标签（labelIds）与分类分组（groupingId），提交后进入审核（UNDER_REVIEW），通过后公开展示。
- **小记（create_time_note）**：按日期记录的轻量内容。一段感悟、一条经验、当日见闻。无需审核，按记录日期归档到时光小记。同一素材若不足 300 字或没有明确主题，优先用小记。

## 标签与专栏

- 标签从 get_publish_context 返回的 labels 里选 1~4 个，优先选已有标签，不要每次都造新概念；确实没有贴切的再提示用户创建。
- 分类分组（groupingId）决定文章出现在哪个栏目，选最贴切的那个，拿不准时先问用户。
- 专栏（specialId）是用户的系列文章集合，仅在用户明确说"放进我的 XX 专栏"时传入。

## tool 调用顺序建议

1. `list_skills` → `get_skill`：读取本规范与用户的个人写作偏好（如有自建 skill，用户偏好优先）。
2. `get_publish_context`：拿当前可选标签、分类分组、专栏与笔名。
3. 需要配图时 `upload_image` 上传拿 url，再在正文 Markdown 里引用。
4. `create_article` / `create_time_note` 提交；修改用 `update_article` / `update_time_note`，不要重复创建。

## 审核规则

所有文章提交后状态为 UNDER_REVIEW（审核中），由平台审核通过后才对外可见。审核关注：原创性、排版可读性、正文 Markdown 结构（标题层级、代码块语言标注、图片有 alt）。审核不通过会标注原因，用 `list_my_articles` 查看状态后 `update_article` 修改重提。小记不审核，但请保持内容真实、友善。', 1, NOW());
