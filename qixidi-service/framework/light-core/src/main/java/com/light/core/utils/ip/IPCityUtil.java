package com.light.core.utils.ip;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.InputStream;

/**
 * IP 归属地本地离线查询（ip2region xdb 全内存索引，微秒级，无外部接口依赖）
 *
 * @author zi-wei
 * @create 2025/4/21 15:59
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IPCityUtil {

    private static final Searcher SEARCHER;

    static {
        Searcher s = null;
        try (InputStream in = IPCityUtil.class.getClassLoader().getResourceAsStream("db/ip2region.xdb")) {
            if (in != null) {
                s = Searcher.newWithBuffer(in.readAllBytes());
            }
        } catch (Exception e) {
            log.error("ip2region.xdb 加载失败，IP归属地查询将不可用", e);
        }
        SEARCHER = s;
    }

    /**
     * 查询 IPv4 归属地
     *
     * @param ip IPv4 地址（IPv6 环回请先转 127.0.0.1，IPv6 公网地址库不支持）
     * @return 形如「中国四川成都（电信）」；无法识别返回 null
     */
    public static String getIpCity(String ip) {
        if (SEARCHER == null || ip == null || ip.isBlank()) {
            return null;
        }
        try {
            return format(SEARCHER.search(ip));
        } catch (Exception e) {
            // 非法 IP 格式等：归属地是装饰信息，warn 即可不视为系统错误
            log.warn("IP归属地查询异常: {}", ip, e);
            return null;
        }
    }

    /**
     * xdb 返回「国家|区域|省份|城市|ISP」五段，过滤占位值 "0" 后拼接
     */
    private static String format(String region) {
        if (region == null || region.isEmpty()) {
            return null;
        }
        String[] parts = region.split("\\|");
        StringBuilder sb = new StringBuilder(part(parts, 0))
                .append(part(parts, 2))
                .append(part(parts, 3));
        String isp = part(parts, 4);
        if (!isp.isEmpty()) {
            sb.append("（").append(isp).append("）");
        }
        return sb.isEmpty() ? null : sb.toString();
    }

    private static String part(String[] arr, int index) {
        if (index >= arr.length) {
            return "";
        }
        String v = arr[index];
        return (v == null || v.isEmpty() || "0".equals(v)) ? "" : v;
    }
}
