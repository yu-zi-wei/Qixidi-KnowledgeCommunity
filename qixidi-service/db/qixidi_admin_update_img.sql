-- 更新图片地址（更换文件存储位置时执行）

-- 文章表
UPDATE b_article_information
SET article_cover = REPLACE ( article_cover, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' ),
    article_content = REPLACE ( article_content, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' ),
    article_content_md = REPLACE ( article_content_md, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );

-- 专辑表
UPDATE b_dictum_album
SET cover = REPLACE ( cover, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );

-- 用户表
UPDATE b_user_main
SET avatar = REPLACE ( avatar, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );

-- 名言分组
UPDATE b_dictum_group
SET cover = REPLACE ( cover, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );


-- 名言信息表
UPDATE b_dictum_info
SET content = REPLACE ( content, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' ),
    content_md = REPLACE ( content_md, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );


-- 专栏表
UPDATE b_special_information
SET cover = REPLACE ( cover, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );

-- 反馈表
UPDATE b_feedback
SET feedback_content = REPLACE ( feedback_content, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );

-- 时光小记
UPDATE b_time_notes
SET content = REPLACE ( content, 'https://minio.qixidi.top/qixidi', 'https://oss-qixidi.oss-cn-chengdu.aliyuncs.com' );
