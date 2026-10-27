package com.qixidi.mcp.config;

import com.qixidi.mcp.tools.PingTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MCP server 装配：注册对外暴露的 tools
 * 后续业务 tool 在此追加 toolObjects
 */
@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider tools(PingTool pingTool) {
        return MethodToolCallbackProvider.builder()
            .toolObjects(pingTool)
            .build();
    }
}
