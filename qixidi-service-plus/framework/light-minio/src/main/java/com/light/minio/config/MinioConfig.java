package com.light.minio.config;

import io.minio.MinioClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zi-wei
 * @create 2025/2/19 15:53
 */
@ConfigurationProperties(prefix = "minio.storage")
@Configuration
@Data
public class MinioConfig {

    /**
     * 上传的桶
     */
    private String bucketName;

    /**
     * 上传端口
     */
    private String endpoint;

    /**
     * 外部访问端口
     */
    private String accessPort;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;


    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
    }
}
