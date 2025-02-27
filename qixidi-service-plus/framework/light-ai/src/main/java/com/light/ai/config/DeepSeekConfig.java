package com.light.ai.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

/**
 * @author zi-wei
 * @create 2025/2/27 15:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@ConfigurationProperties(prefix = "deepSeek.config")
@Configuration
public class DeepSeekConfig {
    /**
     * 官网：https://cloud.siliconflow.cn/models
     */

    /**
     * 密钥
     */
    private String AuthorizationKey = "sk-qrkzlmqqarpykhoeqfwwoidxhvyhvxeekmwxsqttwcfusyps";

    /**
     * 地址
     */
    private String url = "https://api.siliconflow.cn/v1/chat/completions";

    /**
     * 模型
     */
    private String model = "deepseek-ai/DeepSeek-R1-Distill-Qwen-32B";

    /**
     * 请求角色
     */
    private String role = "user";

}
