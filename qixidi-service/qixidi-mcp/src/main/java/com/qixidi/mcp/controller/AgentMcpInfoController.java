package com.qixidi.mcp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * MCP 接入信息（端点地址 + 各客户端配置片段，前端直接渲染）
 *
 * @author zi-wei
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/agent/mcp-info")
public class AgentMcpInfoController {

    /**
     * 生产端点经 nginx 反代（location /mcp 保留原路径），公网地址固定
     */
    private static final String ENDPOINT = "https://qixidi.top/mcp";
    private static final String HEADER_NAME = "X-API-Key";

    @GetMapping
    public Map<String, Object> info() {
        return Map.of(
            "endpoint", ENDPOINT,
            "headerName", HEADER_NAME,
            "snippets", Map.of(
                "claudeCode", "claude mcp add qixidi --transport http " + ENDPOINT
                    + " --header \"X-API-Key: <你的API Key>\"",
                "codex", "# ~/.codex/config.toml\n[mcp_servers.qixidi]\nurl = \"" + ENDPOINT
                    + "\"\nhttp_headers = { \"" + HEADER_NAME + "\" = \"<你的API Key>\" }",
                "generic", "{\"mcpServers\":{\"qixidi\":{\"type\":\"http\",\"url\":\"" + ENDPOINT
                    + "\",\"headers\":{\"X-API-Key\":\"<你的API Key>\"}}}}"
            )
        );
    }
}
