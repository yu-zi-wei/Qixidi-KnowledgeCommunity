package com.light.core.config;

import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码初始化
 *
 * @author zi-wei
 * @create 2025/2/7 15:58
 */
@Configuration
public class CaptchaBeanConfig {
    private final int codeWidth = 160;
    private final int codeHeight = 60;

    @Bean
    public LineCaptcha lineCaptcha() {
        return new LineCaptcha(codeWidth, codeHeight);
    }

    @Bean
    public CircleCaptcha circleCaptcha() {
        return new CircleCaptcha(codeWidth, codeHeight);
    }

    @Bean
    public ShearCaptcha shearCaptcha() {
        return new ShearCaptcha(codeWidth, codeHeight);
    }
}
