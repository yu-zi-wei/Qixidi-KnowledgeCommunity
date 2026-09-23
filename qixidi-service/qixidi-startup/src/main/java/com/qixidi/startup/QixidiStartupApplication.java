package com.qixidi.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableAsync //支持异步注解
@EnableScheduling //支持定时任务
@ComponentScan(basePackages = {"com.qixidi", "com.light"})
@SpringBootApplication
public class QixidiStartupApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(QixidiStartupApplication.class, args);
        printStartupBanner(context);
    }

    /**
     * 启动完成后打印站点横幅（ANSI 颜色，IDEA/Terminal 控制台渲染）
     */
    private static void printStartupBanner(ConfigurableApplicationContext context) {
        String cyan = "\u001b[96m";
        String dim = "\u001b[90m";
        String reset = "\u001b[0m";
        String line = "─".repeat(35);

        String[] profiles = context.getEnvironment().getActiveProfiles();
        String profile = profiles.length > 0 ? String.join("/", profiles) : "default";
        String port = context.getEnvironment().getProperty("server.port", "8080");

        String[] banner = {
            cyan + "   ███   ███  █   █  ███  ███   ███",
            cyan + "  █   █   █    █ █   █    █  █   █ ",
            cyan + "  █   █   █     █    █    █  █   █ ",
            cyan + "  █ █ █   █    █ █   █    █  █   █ ",
            cyan + "   ████  ███  █   █  ███  ███   ███" + reset,
            dim + line + reset,
            "  qixidi · 栖息地启动成功",
            "  环境: " + profile + "    端口: " + port,
            dim + line + reset
        };
        System.out.println(String.join(System.lineSeparator(), banner));
    }

}
