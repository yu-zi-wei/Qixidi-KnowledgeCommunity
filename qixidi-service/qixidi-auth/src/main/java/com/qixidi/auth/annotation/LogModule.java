package com.qixidi.auth.annotation;

import java.lang.annotation.*;

/**
 * 操作日志模块注解（标在 Controller 类上）
 *
 * 与方法级 {@link Log} 配合：@LogModule 标识业务模块（如"标签管理"），
 * @Log 的 title 标识具体操作功能（如"新增"、"合并"）
 *
 * @author ziwei
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogModule {

    /**
     * 模块名称
     */
    String value();
}
