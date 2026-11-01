package com.qixidi.system.config;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.system.domain.vo.PermGroupVo;
import com.qixidi.system.domain.vo.PermItemVo;
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

    /**
     * 动作词根 → 中文（权限码末段）。新增动作词根时同步登记，未登记回退英文原段
     */
    private static final Map<String, String> ACTION_LABELS = Map.ofEntries(
            Map.entry("add", "新增"),
            Map.entry("edit", "编辑"),
            Map.entry("remove", "删除"),
            Map.entry("list", "列表"),
            Map.entry("query", "查询"),
            Map.entry("export", "导出"),
            Map.entry("merge", "合并"),
            Map.entry("forceLogout", "强制下线"),
            Map.entry("resetPwd", "重置密码"));

    /**
     * 模块首段 → 分组中文标题（口径与后台侧栏菜单一致）
     */
    private static final Map<String, String> MODULE_LABELS = Map.ofEntries(
            Map.entry("album", "时光相册"),
            Map.entry("article", "文章管理"),
            Map.entry("blackList", "IP 黑名单"),
            Map.entry("browsing", "浏览记录"),
            Map.entry("collection", "收藏管理"),
            Map.entry("dictum", "随笔管理"),
            Map.entry("fabulous", "点赞管理"),
            Map.entry("feedback", "用户反馈"),
            Map.entry("friendLink", "友情链接"),
            Map.entry("groupingInfo", "标签分类"),
            Map.entry("labelInfo", "标签管理"),
            Map.entry("monitor", "监控审计"),
            Map.entry("navigation", "导航与侧边栏"),
            Map.entry("news", "消息管理"),
            Map.entry("order", "订单管理"),
            Map.entry("private", "私信管理"),
            Map.entry("rechargeInfo", "充值记录"),
            Map.entry("role", "角色管理"),
            Map.entry("search", "搜索记录"),
            Map.entry("shield", "屏蔽词库"),
            Map.entry("sidebar", "导航与侧边栏"),
            Map.entry("site", "站点文件"),
            Map.entry("special", "专栏列表"),
            Map.entry("timeNotes", "小记管理"),
            Map.entry("toolInfo", "工具管理"),
            Map.entry("tripartiteUser", "前台用户"),
            Map.entry("user", "后台账号"));

    /**
     * 对象前缀（权限码去掉末段）→ 中文对象名。新增业务对象权限码时同步登记，未登记回退模块名/英文原段
     */
    private static final Map<String, String> OBJECT_LABELS = Map.ofEntries(
            Map.entry("album", "时光相册"),
            Map.entry("article:comment", "文章评论"),
            Map.entry("article:information", "文章"),
            Map.entry("blackList", "IP 黑名单"),
            Map.entry("browsing:history", "浏览记录"),
            Map.entry("collection:information", "收藏"),
            Map.entry("dictum:album", "随笔专辑"),
            Map.entry("dictum:comment", "随笔评论"),
            Map.entry("dictum:group", "随笔分组"),
            Map.entry("dictum:info", "随笔"),
            Map.entry("fabulous:record", "点赞记录"),
            Map.entry("feedback", "用户反馈"),
            Map.entry("friendLink", "友情链接"),
            Map.entry("groupingInfo", "标签分类"),
            Map.entry("labelInfo", "标签"),
            Map.entry("monitor:cache", "缓存监控"),
            Map.entry("monitor:logininfor", "登录日志"),
            Map.entry("monitor:online", "在线用户"),
            Map.entry("monitor:operlog", "操作日志"),
            Map.entry("monitor:signLog", "签到日志"),
            Map.entry("navigation", "导航菜单"),
            Map.entry("news:systemInfo", "系统消息"),
            Map.entry("news:userInfo", "用户消息"),
            Map.entry("order", "订单"),
            Map.entry("private:news", "私信消息"),
            Map.entry("rechargeInfo", "充值记录"),
            Map.entry("role", "角色"),
            Map.entry("search:records", "搜索记录"),
            Map.entry("shield:shieldWord", "屏蔽词"),
            Map.entry("sidebar", "侧边栏推荐"),
            Map.entry("site:siteFile", "站点文件"),
            Map.entry("special:information", "专栏"),
            Map.entry("timeNotes", "小记"),
            Map.entry("timeNotes:comment", "小记评论"),
            Map.entry("toolInfo", "推荐工具"),
            Map.entry("tripartiteUser", "前台用户"),
            Map.entry("user", "后台账号"),
            Map.entry("user:report", "用户举报"));

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
                .map(e -> new PermGroupVo(e.getKey(),
                        MODULE_LABELS.getOrDefault(e.getKey(), e.getKey()),
                        e.getValue().stream().map(PermissionScanner::toPermItem).toList()))
                .sorted(Comparator.comparing(PermGroupVo::getModule))
                .toList();
        log.info("权限码注册完成：{} 个模块 / {} 个权限码", groups.size(), perms.size());
    }

    /**
     * 权限码 → 中文条目：对象前缀 + 动作词根拼「对象-动作」（如 article:comment:remove → 文章评论-删除）；
     * 未登记词根回退英文原段，新权限码缺词一眼可辨
     */
    private static PermItemVo toPermItem(String perm) {
        int idx = perm.lastIndexOf(':');
        String object = idx > 0 ? perm.substring(0, idx) : perm;
        String action = idx > 0 ? perm.substring(idx + 1) : perm;
        String label = OBJECT_LABELS.getOrDefault(object, MODULE_LABELS.getOrDefault(object, object))
                + "-" + ACTION_LABELS.getOrDefault(action, action);
        return new PermItemVo(perm, label);
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
