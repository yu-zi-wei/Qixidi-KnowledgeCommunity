-- 相册表：图片来源于时光小记（source=1）/文章（source=2 预留）内容中自动解析的图片
CREATE TABLE `b_album` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'id',
    `uid`         VARCHAR(64)  NOT NULL COMMENT '图片归属用户uuid',
    `source_id`   BIGINT       NOT NULL COMMENT '来源内容id（小记id/文章id）',
    `source_time` DATE         DEFAULT NULL COMMENT '来源内容时间（小记record_time/文章发布时间）',
    `source`      TINYINT      NOT NULL DEFAULT 1 COMMENT '来源：1-时光小记，2-文章',
    `url`         VARCHAR(2048) NOT NULL COMMENT '图片地址',
    `create_by`   VARCHAR(64)  DEFAULT '' COMMENT '创建者',
    `create_time` DATETIME     DEFAULT NULL COMMENT '创建时间',
    `update_by`   VARCHAR(64)  DEFAULT '' COMMENT '更新者',
    `update_time` DATETIME     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_uid` (`uid`),
    KEY `idx_source` (`source_id`, `source`),
    KEY `idx_source_time` (`source_time`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT = '相册表';
