package com.aurora.common.utils.ip;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.http.HttpUtil;
import com.aurora.common.config.RuoYiConfig;
import com.aurora.common.constant.Constants;
import com.aurora.common.utils.JsonUtils;
import com.aurora.common.utils.StringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * 获取ip地址归属地
     *
     * @param ip
     * @return
     */
    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        if (StringUtils.isBlank(ip)) {
            return address;
        }
        // 内网不查询
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip);
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        if (RuoYiConfig.isAddressEnabled()) {
            try {
                String rspStr = HttpUtil.createGet(IP_URL)
                    .body("ip=" + ip + "&json=true", Constants.GBK)
                    .execute()
                    .body();
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return UNKNOWN;
                }
                Dict obj = JsonUtils.parseMap(rspStr);
                String region = obj.getStr("pro");
                String city = obj.getStr("city");
                return String.format("%s %s", region, city);
            } catch (Exception e) {
                log.error("获取地理位置异常 {}", ip);
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
//        hutoos工具类获取
//        String clientIP = ServletUtil.getClientIP(request, null);

        // 一般项目会配置反向代理等，所以 x-forwarded-for 有时获取不到 用户真实地址
        // 故而 加上 Proxy-Client-IP，WL-Proxy-Client-IP （此两条为 apache 对 request 的封装）
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
