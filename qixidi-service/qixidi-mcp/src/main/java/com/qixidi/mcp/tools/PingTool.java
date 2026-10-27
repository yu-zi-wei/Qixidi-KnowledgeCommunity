package com.qixidi.mcp.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * MCP 连通性测试工具
 */
@Component
public class PingTool {

    @Tool(description = "连通性测试：可传入任意文本，返回 pong，用于验证 qixidi MCP 连接是否正常")
    public String ping(@ToolParam(required = false, description = "可选回显文本") String text) {
        return (text == null || text.isBlank()) ? "pong" : "pong: " + text;
    }
}
