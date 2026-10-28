package com.qixidi.mcp.controller;

import com.qixidi.mcp.domain.vo.mcp.McpToolSummaryVo;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * MCP 接入信息（端点地址 + 各客户端配置片段，前端直接渲染）；
 * endpoint/headerName 走 qixidi.mcp.agent-info.* 配置统一管理
 *
 * @author zi-wei
 */
@RestController
@RequestMapping("/agent/mcp-info")
public class AgentMcpInfoController {

    /**
     * notice：key 明文即完整前台会话凭证（会话桥），必须向用户明示保管责任
     */
    private static final String NOTICE = "API key 等同你的完整账号凭证，请像密码一样保管；泄露或不再使用请立即吊销";

    private final String endpoint;

    private final String headerName;

    private final ToolCallbackProvider toolCallbackProvider;

    public AgentMcpInfoController(
        @Value("${qixidi.mcp.agent-info.endpoint}") String endpoint,
        @Value("${qixidi.mcp.agent-info.header-name}") String headerName,
        ToolCallbackProvider toolCallbackProvider) {
        this.endpoint = endpoint;
        this.headerName = headerName;
        this.toolCallbackProvider = toolCallbackProvider;
    }

    @GetMapping
    public Map<String, Object> info() {
        return Map.of(
            "endpoint", endpoint,
            "headerName", headerName,
            "notice", NOTICE,
            "snippets", Map.of(
                "claudeCode", "claude mcp add qixidi --transport http " + endpoint
                    + " --header \"" + headerName + ": <你的API Key>\"",
                "codex", "# ~/.codex/config.toml\n[mcp_servers.qixidi]\nurl = \"" + endpoint
                    + "\"\nhttp_headers = { \"" + headerName + "\" = \"<你的API Key>\" }",
                "generic", "{\"mcpServers\":{\"qixidi\":{\"type\":\"http\",\"url\":\"" + endpoint
                    + "\",\"headers\":{\"" + headerName + "\":\"<你的API Key>\"}}}}"
            )
        );
    }

    /**
     * 当前注册的 MCP tool 清单（聚合自 ToolCallbackProvider，工具增减自动同步）
     */
    @GetMapping("/tools")
    public List<McpToolSummaryVo> tools() {
        return Arrays.stream(toolCallbackProvider.getToolCallbacks())
            .map(callback -> {
                var definition = callback.getToolDefinition();
                McpToolSummaryVo vo = new McpToolSummaryVo();
                vo.setName(definition.name());
                vo.setDescription(definition.description());
                return vo;
            })
            .sorted(Comparator.comparing(McpToolSummaryVo::getName))
            .toList();
    }
}
