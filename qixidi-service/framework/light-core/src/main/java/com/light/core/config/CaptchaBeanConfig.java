package com.light.core.config;

import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.ShearCaptcha;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.awt.*;

/**
 * 验证码初始化
 *
 * @author zi-wei
 * @create 2025/2/7 15:58
 */
@Configuration
public class CaptchaBeanConfig {
    private final int codeWidth = 160;
    private final int codeHeight = 40;
    private static final Color BACKGROUND = Color.PINK;
    private static final Font FONT = new Font("Arial", Font.BOLD, 48);

    @Bean
    public LineCaptcha lineCaptcha() {
        LineCaptcha lineCaptcha = new LineCaptcha(codeWidth, codeHeight);
        lineCaptcha.setBackground(BACKGROUND);
        lineCaptcha.setFont(FONT);
        return lineCaptcha;
    }

    @Bean
    public CircleCaptcha circleCaptcha() {
        CircleCaptcha circleCaptcha = new CircleCaptcha(codeWidth, codeHeight);
        circleCaptcha.setBackground(BACKGROUND);
        circleCaptcha.setFont(FONT);
        return circleCaptcha;
    }

    @Bean
    public ShearCaptcha shearCaptcha() {
        ShearCaptcha shearCaptcha = new ShearCaptcha(codeWidth, codeHeight);
        shearCaptcha.setBackground(BACKGROUND);
        shearCaptcha.setFont(FONT);
        return shearCaptcha;
    }
}
