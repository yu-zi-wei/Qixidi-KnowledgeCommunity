package com.light.core.utils.ip;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import com.light.core.config.LightConfig;
import com.light.core.constant.Constants;
import com.light.core.utils.JsonUtils;
import com.light.core.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取地址类
 *
 * @author Lion Li
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtils {

    // IP地址查询
    public static final String IP_URL = "http://whois.pconline.com.cn/ipJson.jsp";

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    // 查询失败的 IP 缓存（ip → 失败时间戳）：外部接口失败后短期内不再重试，避免调用方（如黑名单落库定时任务）反复请求+刷日志
    private static final Map<String, Long> FAIL_CACHE = new ConcurrentHashMap<>();

    // 失败缓存有效期
    private static final long FAIL_CACHE_MILLIS = 10 * 60 * 1000L;

    /**
     * 获取ip地址归属地
     * <p>
     * 归属地仅用于日志/黑名单展示，外部接口失败返回 {@link #UNKNOWN}，不视为系统错误
     *
     * @param ip
     * @return
     */
    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        if (StringUtils.isBlank(ip)) {
            return address;
        }
        // 先清理 HTML 标签（防止 XSS）
        ip = HtmlUtil.cleanHtmlTag(ip);
        // 统一将 IPv6 环回地址转为 IPv4 形式
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) {
            ip = "127.0.0.1";
        }
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        // 短期内查询失败过的 IP 直接返回未知
        Long failAt = FAIL_CACHE.get(ip);
        if (failAt != null) {
            if (System.currentTimeMillis() - failAt < FAIL_CACHE_MILLIS) {
                return UNKNOWN;
            }
            FAIL_CACHE.remove(ip);
        }
        if (LightConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtil.createGet(IP_URL)
                        .body("ip=" + ip + "&json=true", Constants.GBK)
                        .timeout(3000)
                        .execute()
                        .body();
                if (StringUtils.isEmpty(rspStr)) {
                    log.warn("获取地理位置失败，返回空响应: {}", ip);
                    FAIL_CACHE.put(ip, System.currentTimeMillis());
                    return UNKNOWN;
                }
                Dict obj = JsonUtils.parseMap(rspStr);
                String region = obj.getStr("pro");
                String city = obj.getStr("city");
                if (StringUtils.isBlank(region) && StringUtils.isBlank(city)) {
                    log.warn("获取地理位置失败，归属地为空: {}", ip);
                    FAIL_CACHE.put(ip, System.currentTimeMillis());
                    return UNKNOWN;
                }
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.warn("获取地理位置异常: {}", ip, e);
                FAIL_CACHE.put(ip, System.currentTimeMillis());
            }
        }
        return UNKNOWN;
    }

    /**
     * 获取客户端ip地址
     *
     * @param request
     * @return
     */
    public static String gainIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
