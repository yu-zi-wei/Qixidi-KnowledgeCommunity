package com.qixidi.auth.config;

import com.qixidi.auth.interceptor.CurrentLimitingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zi-wei
 * @create 2025/2/14 15:19
 */
@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        全局接口限流（防刷接口）
        registry.addInterceptor(new CurrentLimitingInterceptor());
//        // 全局链路跟踪拦截器
//        registry.addInterceptor(new TLogWebInterceptor());
//        // 全局访问性能拦截
//        registry.addInterceptor(new PlusWebInvokeTimeInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }
}
