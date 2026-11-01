package com.qixidi.auth.annotation;


import java.lang.annotation.*;

/**
 * 权限中文名伴生注解：标在 @SaCheckPermission 同一方法上，为角色权限树提供中文展示名
 *
 * PermissionScanner 读取本注解作为权限码中文名（如 "文章评论-新增"）；
 * 未标注时回退权限码原值（英文原段一眼可辨，倒逼补标注）。
 * 同一权限码多个方法须保持文案一致，不一致时扫描器保留首个并打 WARN。
 *
 * @author ziwei
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermName {

    /**
     * 权限中文名（如 "文章评论-新增"）
     */
    String value() default "";
}
