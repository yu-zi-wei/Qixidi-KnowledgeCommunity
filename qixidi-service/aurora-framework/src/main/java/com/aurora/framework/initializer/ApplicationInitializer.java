package com.aurora.framework.initializer;

import com.aurora.common.utils.context.SpringContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author ziwei
 * @date 2024年01月07日
 */

@Component
public class ApplicationInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 获取 ServletContext
        ServletContext servletContext = sce.getServletContext();

        // 通过 WebApplicationContextUtils 获取 ApplicationContext
        ApplicationContext applicationContext =
            WebApplicationContextUtils.getWebApplicationContext(servletContext);

        // 将 ApplicationContext 设置给 SpringContext
        SpringContext.inst().setApplicationContext(applicationContext);
        System.out.println("初始化----------applicationContext");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // 项目关闭时的清理操作，如果需要的话
    }
}
