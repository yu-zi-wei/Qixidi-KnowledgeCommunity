package com.aurora.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zi-wei
 * @create 2024/11/17 21:06
 */
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/white")
public class WhiteTestController {

    @Value("${spring.redis.host}")
//    @Value("${sa-token.activity-timeout}")
    private String name;

    @GetMapping("/name")
    public String list() {
        return name;
    }
}
