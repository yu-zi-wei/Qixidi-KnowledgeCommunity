package com.aurora.web.controller;

import com.aurora.common.config.justAuth.JustAuthConfig;
import com.aurora.common.config.justAuth.WeiBoPlatformConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 白名单测试
 *
 * @author zi-wei
 * @create 2024/11/17 21:06
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/white")
public class WhiteTestController {

    @Value("${spring.redis.host}")
    private String name;

    @Autowired
    private JustAuthConfig justAuthConfig;

    @Autowired
    private WeiBoPlatformConfig weiBoPlatformConfig;

    @GetMapping("/name")
    public String list() {
        log.info("weiBoPlatformConfig:{}", weiBoPlatformConfig.toString());
        return "xx";
    }
}
