package com.qixidi.mcp.auth;

import com.light.core.enums.MsgEnums;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserRoleEnums;
import com.qixidi.mcp.service.ApiKeyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * /mcp 端点 API key 认证过滤器。
 *
 * 校验通过后把 X-API-Key 换头成 Authorization: Bearer {key} 放行，
 * Sa-Token 体系即可识别签发时创建的会话，业务侧 LoginHelper 原生可用。
 * 注意：拒绝时必须显式写状态码 + JSON body，只 return 不写响应会变成 200 + 空 body。
 * 全局门槛：MCP 服务仅限创作者/管理员（key 签发时校验，此处兜底拦截被降级用户的存量 key）。
 *
 * @author zi-wei
 */
@Slf4j
@RequiredArgsConstructor
public class McpApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";
    /** 403 提示文案会透传给 agent 用户，引导其去网站申请创作者 */
    private static final String NOT_CREATOR_MSG = "MCP 服务仅限创作者或管理员使用，请先在栖息地网站申请成为创作者";

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
            TripartiteUser user = apiKeyService.validate(rawKey);
            // 全局角色门槛：非创作者/管理员的一切 MCP 请求（含 tools/call）全拒
            if (!UserRoleEnums.getAdvancedRoleList().contains(user.getRoleId())) {
                log.warn("MCP API key 所属用户非创作者（roleId={}，{}），已拒绝", user.getRoleId(), request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"msg\":\"" + NOT_CREATOR_MSG + "\",\"data\":null}");
                return;
            }
        } catch (Exception e) {
            // 打日志再拒：瞬时基础设施抖动与真正的坏 key 在响应上无法区分，静默吞掉会变成无头案
            log.warn("MCP API key 校验失败（{}）：{}", request.getRequestURI(), e.getMessage());
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
