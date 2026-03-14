package com.light.core.constant;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 系统常量
 *
 * @author zi-wei
 * @create 2024/11/10 18:21
 */

@Component
public class SystemConstant {

    private static List<String> administratorMailboxList;

    @Value("${system.admin.mailboxes}")
    private String adminMailboxesStr;

    @PostConstruct  // Spring 初始化后执行
    public void init() {
        administratorMailboxList = Arrays.asList(adminMailboxesStr.split(","));
    }

    public static List<String> getAdministratorMailboxList() {
        return administratorMailboxList;
    }

}
