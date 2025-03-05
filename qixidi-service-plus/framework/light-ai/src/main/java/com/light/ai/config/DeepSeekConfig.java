package com.light.ai.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author zi-wei
 * @create 2025/2/27 15:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "deep-seek.config")
@Configuration
public class DeepSeekConfig {
    /**
     * 官网：https://cloud.siliconflow.cn/models
     */

    /**
     * 密钥
     */
    private String authorizationKey;

    /**
     * 地址
     */
    private String url;

    /**
     * 模型
     */
    private String model;

    /**
     * 请求角色
     */
    private String role;

}
