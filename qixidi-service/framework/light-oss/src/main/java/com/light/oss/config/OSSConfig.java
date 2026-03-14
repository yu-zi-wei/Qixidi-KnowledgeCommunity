package com.light.oss.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zi-wei
 * @create 2025/2/20 14:14
 */
@ConfigurationProperties(prefix = "aliyun.oss")
@Configuration
@Data
public class OSSConfig {

    /**
     * 区域
     */
    private String endPoint;

    /**
     * accessKeyId
     */
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * 上传的桶
     */
    private String bucketName;

    /**
     * http协议
     */
    private String httpProtocol;

    /**
     * 开关
     */
    private Boolean enabledSwitch = false;

}
