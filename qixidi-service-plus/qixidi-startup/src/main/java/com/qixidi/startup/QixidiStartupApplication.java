package com.qixidi.startup;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qixidi", "com.light"})
public class QixidiStartupApplication {

    public static void main(String[] args) {
        SpringApplication.run(QixidiStartupApplication.class, args);
    }

}
