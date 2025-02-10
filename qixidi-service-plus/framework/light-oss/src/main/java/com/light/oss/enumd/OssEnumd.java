package com.light.oss.enumd;

import com.light.oss.service.impl.AliyunOssStrategy;
import com.light.oss.service.impl.MinioOssStrategy;
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
     * 阿里云
     */
    ALIYUN("aliyun", AliyunOssStrategy.class),

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
