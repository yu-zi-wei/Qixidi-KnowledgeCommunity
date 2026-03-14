package com.light.core.config.justAuth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zi-wei
 * @create 2024/11/23 11:42
 */
@Data
@Component
@ConfigurationProperties(prefix = "justauth.platform.weibo")
public class WeiBoPlatformConfig {
    private String clientId;
    private String clientSecret;
    private String redirectUrl;
}
