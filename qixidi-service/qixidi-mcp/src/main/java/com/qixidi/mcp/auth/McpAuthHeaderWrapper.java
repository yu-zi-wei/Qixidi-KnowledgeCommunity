package com.qixidi.mcp.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

/**
 * 请求头包装：把 Authorization 头替换为 "Bearer {apiKey}"，
 * 使 Sa-Token（token-name=Authorization, token-prefix=Bearer）识别 MCP 会话。
 *
 * @author zi-wei
 */
public class McpAuthHeaderWrapper extends HttpServletRequestWrapper {

    private static final String AUTH_HEADER = "Authorization";

    private final String replacedValue;

    public McpAuthHeaderWrapper(HttpServletRequest request, String apiKey) {
        super(request);
        this.replacedValue = "Bearer " + apiKey;
    }

    @Override
    public String getHeader(String name) {
        if (AUTH_HEADER.equalsIgnoreCase(name)) {
            return replacedValue;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (AUTH_HEADER.equalsIgnoreCase(name)) {
            return Collections.enumeration(List.of(replacedValue));
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        if (!names.contains(AUTH_HEADER)) {
            names.add(AUTH_HEADER);
        }
        return Collections.enumeration(names);
    }
}
