package com.aurora.common.config.justAuth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author zi-wei
 * @create 2024/11/23 11:39
 */
@Data
@Component
@ConfigurationProperties(prefix = "justauth")
@AllArgsConstructor
@NoArgsConstructor
public class JustAuthConfig {
    /**
     * 前端中转地址 与前端默认地址保持一致
     */
    private String transferUrl;
    /**
     * 第三方登录类型 用于获取用户信息方式判断
     */
    public String tripartiteUserType;
}
