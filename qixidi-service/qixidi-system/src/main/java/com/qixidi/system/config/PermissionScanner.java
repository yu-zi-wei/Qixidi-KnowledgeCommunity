package com.qixidi.system.config;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.system.domain.vo.PermGroupVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 权限码注册器：启动后扫描全部 @SaCheckPermission 注解，生成权限码清单
 *
 * 权限码字典不落库（跟着 Controller 代码走），本类是唯一权威来源：
 * - /system/perm/list 输出给前端角色管理的勾选树
 * - 角色分配界面只能勾到清单内的码，与代码永不脱节
 *
 * @author ziwei
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionScanner implements SmartInitializingSingleton {

    private final RequestMappingHandlerMapping handlerMapping;

    /**
     * 按模块首段分组的权限码清单（初始化后只读）
     */
    private List<PermGroupVo> groups = List.of();

    @Override
    public void afterSingletonsInstantiated() {
        Set<String> perms = new TreeSet<>();
        handlerMapping.getHandlerMethods().forEach((info, handlerMethod) -> {
            collectFromMethod(handlerMethod, perms);
        });
        Map<String, Set<String>> byModule = new LinkedHashMap<>();
        for (String perm : perms) {
            String module = perm.split(":")[0];
            byModule.computeIfAbsent(module, k -> new TreeSet<>()).add(perm);
        }
        groups = byModule.entrySet().stream()
                .map(e -> new PermGroupVo(e.getKey(), List.copyOf(e.getValue())))
                .sorted(Comparator.comparing(PermGroupVo::getModule))
                .toList();
        log.info("权限码注册完成：{} 个模块 / {} 个权限码", groups.size(), perms.size());
    }

    private void collectFromMethod(HandlerMethod handlerMethod, Set<String> perms) {
        SaCheckPermission methodPerm = handlerMethod.getMethodAnnotation(SaCheckPermission.class);
        if (methodPerm != null) {
            perms.addAll(List.of(methodPerm.value()));
        }
        SaCheckPermission classPerm = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), SaCheckPermission.class);
        if (classPerm != null) {
            perms.addAll(List.of(classPerm.value()));
        }
    }

    public List<PermGroupVo> getGroups() {
        return groups;
    }
}
