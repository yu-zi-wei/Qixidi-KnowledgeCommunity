package com.qixidi.auth.interceptor;

import com.qixidi.auth.helper.CurrentLimitingHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 接口防刷拦截器
 */
@Slf4j
public class CurrentLimitingInterceptor implements HandlerInterceptor {
    private final CurrentLimitingHandler currentLimitingHandler = new CurrentLimitingHandler();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        if (currentLimitingHandler.currentLimiting(request)) {
            return true;
        }
        // 明确返回 429 JSON，禁止放任 Spring 默认行为输出 200 空 body（排查时极难定位）
        response.setStatus(429);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请稍后再试\"}");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
