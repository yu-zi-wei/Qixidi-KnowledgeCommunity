package com.qixidi.auth.config;

import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.stp.StpUtil;
import com.qixidi.auth.config.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * sa-token 配置
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Slf4j
@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    private final SecurityProperties securityProperties;

    @Bean
    public SaServletFilter getSaServletFilter() {
        return new SaServletFilter()
                // 指定 [拦截路由]
                .addInclude("/**")
                // 指定 [放行路由]
                .addExclude(securityProperties.getExcludes())
                // 认证函数: 每次请求执行
                .setAuth(obj -> {
                    StpUtil.checkLogin();//判断是否登录
                });

    }
}
