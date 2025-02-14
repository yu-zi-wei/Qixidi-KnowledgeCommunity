package com.qixidi.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qixidi", "com.light"})
public class QixidiStartupApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(QixidiStartupApplication.class, args);
        System.out.println("==================> (♥◠‿◠)ﾉﾞ  qi-xi-di Startup success   ლ(´ڡ`ლ)ﾞ  <==================");
    }

}
