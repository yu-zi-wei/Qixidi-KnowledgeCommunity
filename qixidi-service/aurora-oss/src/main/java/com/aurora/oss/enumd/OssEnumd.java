package com.aurora.oss.enumd;

import com.aurora.oss.service.impl.AliyunOssStrategy;
import com.aurora.oss.service.impl.MinioOssStrategy;
import com.aurora.oss.service.impl.QcloudOssStrategy;
import com.aurora.oss.service.impl.QiniuOssStrategy;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 对象存储服务商枚举
 *
 * @author Lion Li
 */
@Getter
@AllArgsConstructor
public enum OssEnumd {

    /**
     * 七牛云
     */
    QINIU("qiniu", QiniuOssStrategy.class),

    /**
     * 阿里云
     */
    ALIYUN("aliyun", AliyunOssStrategy.class),

    /**
     * 腾讯云
     */
    QCLOUD("qcloud", QcloudOssStrategy.class),

    /**
     * minio
     */
    MINIO("minio", MinioOssStrategy.class);

    private final String value;

    private final Class<?> beanClass;

    public static OssEnumd find(String value) {
        for (OssEnumd enumd : values()) {
            if (enumd.getValue().equals(value)) {
                return enumd;
            }
        }
        return null;
    }

}
