package com.light.core.utils.ip;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zi-wei
 * @create 2025/4/21 15:59
 */
@Slf4j
public class IPCityUtil {

    private static Searcher searcher;

    static {
        InputStream resourceAsStream = IPCityUtil.class.getClassLoader().getResourceAsStream("db/ip2region.xdb");
        if (resourceAsStream != null) {
            byte[] cBuff;
            try (resourceAsStream) {
                cBuff = IOUtils.toByteArray(resourceAsStream);
                searcher = Searcher.newWithBuffer(cBuff);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getIpCity(String ip) {
        long ipLong = ipToLong(ip);
        StringBuffer ipCity = new StringBuffer();
        try {
            String search = searcher.search(ipLong);
            if (search == null || search.isEmpty()) return null;
            String[] s1 = search.split("\\|");
            if (s1.length == 0) return ipCity.toString();
            ipCity.append(s1[0]);//国家
            ipCity.append(s1[2]);//省份
            ipCity.append(s1[3]);//市
            ipCity.append("（" + s1[4] + "）");//运营商
            return ipCity.toString();
        } catch (Exception e) {
            log.error("获取IP对应地区异常IP:{}", ip, e);
        }
        return ipCity.toString();
    }

    /**
     * ip转long
     *
     * @param ip
     * @return
     */
    public static long ipToLong(String ip) {
        String[] split = ip.split("\\.");
        long i1 = 0L;
        i1 += Long.parseLong(split[0]) << 24;
        i1 += Long.parseLong(split[1]) << 16;
        i1 += Long.parseLong(split[2]) << 8;
        return i1 + Long.parseLong(split[3]);
    }
}
