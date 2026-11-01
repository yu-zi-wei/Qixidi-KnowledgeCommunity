package com.qixidi.system.config;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.auth.annotation.PermName;
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
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * 权限码注册器：启动后扫描全部 @SaCheckPermission 注解，生成权限码清单
 *
 * 权限码字典不落库（跟着 Controller 代码走），本类是唯一权威来源：
 * - /system/perm/list 输出给前端角色管理的勾选树
 * - 角色分配界面只能勾到清单内的码，与代码永不脱节
 *
 * 权限中文名来自同方法 @PermName 注解（声明式，就近维护）；
 * 未标注回退权限码原值（英文原段一眼可辨，倒逼补标注）
 *
 * @author ziwei
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PermissionScanner implements SmartInitializingSingleton {

    /**
     * 动作词根 → 组内展示顺序（读操作在前：列表 → 详情，写操作随后）。未登记词根排最后（按码字典序）
     */
    private static final Map<String, Integer> ACTION_ORDER = Map.ofEntries(
            Map.entry("list", 0),
            Map.entry("query", 1),
            Map.entry("add", 2),
            Map.entry("edit", 3),
            Map.entry("remove", 4),
            Map.entry("export", 5),
            Map.entry("merge", 6),
            Map.entry("forceLogout", 7),
            Map.entry("resetPwd", 8));

    /**
     * 模块首段 → 分组中文标题（口径与后台侧栏菜单一致）。新增模块时同步登记
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
     * 权限码 → 中文名。初始化后只读；未标 @PermName 的码值为码本身（回退展示）
     */
    private final Map<String, String> permLabels = new TreeMap<>();

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
        handlerMapping.getHandlerMethods().forEach((info, handlerMethod) -> {
            collectFromMethod(handlerMethod);
        });
        Map<String, Set<String>> byModule = new LinkedHashMap<>();
        permLabels.forEach((perm, label) -> {
            String module = perm.split(":")[0];
            byModule.computeIfAbsent(module, k -> new TreeSet<>(PermissionScanner::comparePerm)).add(perm);
        });
        groups = byModule.entrySet().stream()
                .map(e -> new PermGroupVo(e.getKey(),
                        MODULE_LABELS.getOrDefault(e.getKey(), e.getKey()),
                        e.getValue().stream().map(code -> new PermItemVo(code, permLabels.get(code))).toList()))
                .sorted(Comparator.comparing(PermGroupVo::getModule))
                .toList();
        log.info("权限码注册完成：{} 个模块 / {} 个权限码", groups.size(), permLabels.size());
    }

    private void collectFromMethod(HandlerMethod handlerMethod) {
        SaCheckPermission methodPerm = handlerMethod.getMethodAnnotation(SaCheckPermission.class);
        if (methodPerm != null) {
            PermName permName = handlerMethod.getMethodAnnotation(PermName.class);
            String label = permName == null || permName.value().isEmpty() ? null : permName.value();
            for (String code : methodPerm.value()) {
                mergeLabel(code, label);
            }
        }
        SaCheckPermission classPerm = AnnotatedElementUtils.findMergedAnnotation(handlerMethod.getBeanType(), SaCheckPermission.class);
        if (classPerm != null) {
            for (String code : classPerm.value()) {
                // 类级注解无 @PermName 伴生，回退码本身
                mergeLabel(code, null);
            }
        }
    }

    /**
     * 组内排序：按末段动作优先级（ACTION_ORDER），未登记词根排最后；次级按码字典序
     */
    private static int comparePerm(String a, String b) {
        int oa = ACTION_ORDER.getOrDefault(actionOf(a), 99);
        int ob = ACTION_ORDER.getOrDefault(actionOf(b), 99);
        return oa != ob ? Integer.compare(oa, ob) : a.compareTo(b);
    }

    private static String actionOf(String perm) {
        int idx = perm.lastIndexOf(':');
        return idx > 0 ? perm.substring(idx + 1) : perm;
    }

    /**
     * 同码多方法：首个非空中文名生效；回退值（裸码）可被真实中文名覆盖；不同中文名打 WARN 保留首个
     */
    private void mergeLabel(String code, String label) {
        String existing = permLabels.get(code);
        if (label == null || label.isEmpty()) {
            permLabels.putIfAbsent(code, code);
        } else if (existing == null || existing.equals(code)) {
            permLabels.put(code, label);
        } else if (!existing.equals(label)) {
            log.warn("权限码 {} 的 @PermName 文案不一致，保留首个：{} / {}", code, existing, label);
        }
    }

    public List<PermGroupVo> getGroups() {
        return groups;
    }
}
