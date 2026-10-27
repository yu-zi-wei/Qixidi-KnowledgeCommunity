package com.qixidi.mcp.auth;

import com.qixidi.mcp.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * /mcp 端点 API key 认证过滤器。
 *
 * 校验通过后把 X-API-Key 换头成 Authorization: Bearer {key} 放行，
 * Sa-Token 体系即可识别签发时创建的会话，业务侧 LoginHelper 原生可用。
 * 注意：拒绝时必须显式写状态码 + JSON body，只 return 不写响应会变成 200 + 空 body。
 *
 * @author zi-wei
 */
@RequiredArgsConstructor
public class McpApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final ApiKeyService apiKeyService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // 非 MCP 路径不处理（由 FilterRegistrationBean 的 urlPattern 限定，双保险）
        if (!request.getRequestURI().startsWith("/mcp")) {
            filterChain.doFilter(request, response);
            return;
        }
        String rawKey = request.getHeader(API_KEY_HEADER);
        if (rawKey == null || rawKey.isBlank()) {
            reject(response);
            return;
        }
        try {
            apiKeyService.validate(rawKey);
        } catch (Exception e) {
            reject(response);
            return;
        }
        filterChain.doFilter(new McpAuthHeaderWrapper(request, rawKey), response);
    }

    private void reject(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"invalid api key\",\"data\":null}");
    }
}
