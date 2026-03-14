package com.qixidi.auth.interceptor;

import com.qixidi.auth.helper.CurrentLimitingHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 接口防刷拦截器
 */
@Slf4j
public class CurrentLimitingInterceptor implements HandlerInterceptor {
    private final CurrentLimitingHandler currentLimitingHandler = new CurrentLimitingHandler();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return currentLimitingHandler.currentLimiting(request);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
