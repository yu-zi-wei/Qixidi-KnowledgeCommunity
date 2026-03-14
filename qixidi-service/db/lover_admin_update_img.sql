UPDATE lover_carousel
SET img = REPLACE ( img, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );

UPDATE lover_carousel
SET img = REPLACE ( img, 'https://huibao-lover.oss-cn-chengdu.aliyuncs.com', 'https://minio.qixidi.top/lover' );



UPDATE album
SET img = REPLACE ( img, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );

UPDATE album
SET img = REPLACE ( img, 'https://huibao-lover.oss-cn-chengdu.aliyuncs.com', 'https://minio.qixidi.top/lover' );



UPDATE basic_info
SET left_img = REPLACE ( left_img, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' ),
    right_img = REPLACE ( right_img, 'https://zsh-aurora.oss-cn-beijing.aliyuncs.com', 'https://minio.qixidi.top/qixidi' );





UPDATE basic_info
SET left_img = REPLACE ( left_img, 'https://huibao-lover.oss-cn-chengdu.aliyuncs.com', 'https://minio.qixidi.top/lover' ),
    right_img = REPLACE ( right_img, 'https://huibao-lover.oss-cn-chengdu.aliyuncs.com', 'https://minio.qixidi.top/lover' );
