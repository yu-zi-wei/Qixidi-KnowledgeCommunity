package com.qixidi.mcp.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * MCP 连通性测试工具
 */
@Component
public class PingTool {

    @Tool(description = "连通性测试：原样返回输入文本，用于验证 qixidi MCP 连接是否正常")
    public String ping(String text) {
        return "pong: " + text;
    }
}
