package com.qixidi.startup.controller;

/**
 * @author zi-wei
 * @create 2025/2/5 18:49
 */

import com.light.mybatisPlus.domain.entity.SysMenuHidden;
import com.light.mybatisPlus.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
public class userController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public List<SysMenuHidden> add() {
        List<SysMenuHidden> sysMenuHiddens = userService.selectList();
        return sysMenuHiddens;
    }

}
