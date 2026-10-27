package com.qixidi.mcp.config;

import com.qixidi.mcp.auth.McpApiKeyFilter;
import com.qixidi.mcp.service.ApiKeyService;
import com.qixidi.mcp.tools.ArticleTools;
import com.qixidi.mcp.tools.PingTool;
import com.qixidi.mcp.tools.PublishContextTool;
import com.qixidi.mcp.tools.UploadImageTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * MCP server 装配：注册对外暴露的 tools 与 /mcp 认证过滤器
 * 后续业务 tool 在此追加 toolObjects
 */
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider tools(PingTool pingTool, PublishContextTool publishContextTool,
                                      ArticleTools articleTools, UploadImageTool uploadImageTool) {
        return MethodToolCallbackProvider.builder()
            .toolObjects(pingTool, publishContextTool, articleTools, uploadImageTool)
            .build();
    }

    /**
     * /mcp 端点认证过滤器（Servlet Filter 天然先于 Sa-Token 拦截器执行）
     */
    @Bean
    public FilterRegistrationBean<McpApiKeyFilter> mcpApiKeyFilter(ApiKeyService apiKeyService) {
        FilterRegistrationBean<McpApiKeyFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new McpApiKeyFilter(apiKeyService));
        registration.addUrlPatterns("/mcp/*");
        registration.setName("mcpApiKeyFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 100);
        return registration;
    }
}
