package com.qixidi.auth.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.qixidi.auth.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * sa-token 配置
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        // 未登录直接抛 NotLoginException → GlobalExceptionHandler 统一映射 401
        // （不再包装成 ServiceException，否则会变成 500，前端 401 分支永远走不到）
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                //排除路径
                .excludePathPatterns(securityProperties.getExcludes());

    }
}
