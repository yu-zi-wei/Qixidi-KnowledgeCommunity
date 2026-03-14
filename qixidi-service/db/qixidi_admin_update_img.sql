-- 更新图片地址（更换文件存储位置时执行）

-- 文章表
UPDATE b_article_information
SET article_cover = REPLACE ( article_cover, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' ),
    article_content = REPLACE ( article_content, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' ),
    article_content_md = REPLACE ( article_content_md, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );

--专辑表
UPDATE b_dictum_album
SET cover = REPLACE ( cover, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );

-- 用户表
UPDATE b_user_main
SET avatar = REPLACE ( avatar, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );

-- 名言分组
UPDATE b_dictum_group
SET cover = REPLACE ( cover, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );


-- 名言信息表
UPDATE b_dictum_info
SET content = REPLACE ( content, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' ),
    content_md = REPLACE ( content_md, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );


-- 专栏表
UPDATE b_special_information
SET cover = REPLACE ( cover, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );

-- 反馈表
UPDATE b_feedback
SET feedback_content = REPLACE ( feedback_content, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );
