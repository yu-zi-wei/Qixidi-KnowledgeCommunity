package com.light.core.utils.ip;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import com.light.core.config.LightConfig;
import com.light.core.utils.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 获取地址类
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddressUtils {

    // 未知地址
    public static final String UNKNOWN = "XX XX";

    /**
     * 获取ip地址归属地
     * <p>
     * 本地离线库查询（ip2region，见 {@link IPCityUtil}），无外部接口依赖；
     * 归属地仅用于日志/黑名单展示，查不到返回 {@link #UNKNOWN}
     *
     * @param ip
     * @return
     */
    public static String getRealAddressByIP(String ip) {
        if (StringUtils.isBlank(ip)) {
            return UNKNOWN;
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
        if (!LightConfig.isAddressEnabled()) {
            return UNKNOWN;
        }
        String city = IPCityUtil.getIpCity(ip);
        return StringUtils.isBlank(city) ? UNKNOWN : city;
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
