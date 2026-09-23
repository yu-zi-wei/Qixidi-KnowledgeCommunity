package com.qixidi.business.api.frontDesk;

import cn.dev33.satoken.annotation.SaIgnore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author zi-wei
 * @create 2026/9/23 15:29
 */
@RestController
public class CommonController {

    private static LocalDateTime startUpTime = LocalDateTime.now();

    @Value("${spring.profiles.active}")
    private String active;

    /**
     * 服务启动成功页 1
     * # @SaIgnore 忽略权限校验
     *
     * @return
     */
    @SaIgnore
    @GetMapping("/")
    public String welcome() {
        return "【" + active + "】你小子，抄底是吧！服务启动成功-启动时间：" + startUpTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
