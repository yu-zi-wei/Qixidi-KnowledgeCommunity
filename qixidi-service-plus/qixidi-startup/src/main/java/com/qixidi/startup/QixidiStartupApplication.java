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
        ConfigurableApplicationContext run = SpringApplication.run(QixidiStartupApplication.class, args);
        System.out.println("==================> (♥◠‿◠)ﾉﾞ  qi-xi-di Startup success   ლ(´ڡ`ლ)ﾞ  <==================");
    }

}
