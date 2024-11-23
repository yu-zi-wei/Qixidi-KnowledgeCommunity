package com.aurora.common.config.justAuth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zi-wei
 * @create 2024/11/23 11:42
 */
@Data
@Component
@ConfigurationProperties(prefix = "justauth.platform.gitee")
public class GiteePlatformConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
}
