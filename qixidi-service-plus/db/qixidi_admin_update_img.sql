-- 更新图片地址（更换文件存储位置时执行）

-- 文章表
UPDATE b_article_information
SET article_cover = REPLACE ( article_cover, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' ),
    article_content = REPLACE ( article_content, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' ),
    article_content_md = REPLACE ( article_content_md, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' );

--专辑表
UPDATE b_dictum_album
SET cover = REPLACE ( cover, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' );

-- 用户表
UPDATE b_user_main
SET avatar = REPLACE ( avatar, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' );

-- 名言分组
UPDATE b_dictum_group
SET cover = REPLACE ( cover, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' );


-- 名言信息表
UPDATE b_dictum_info
SET content = REPLACE ( content, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' ),
    content_md = REPLACE ( content_md, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' );



-- 专栏表
UPDATE b_special_information
SET cover = REPLACE ( cover, 'http://116.198.203.132:9300/qixidi', 'https://minio.qixidi.top/qixidi' );
