package com.qixidi.auth.helper;

import com.light.core.constant.SystemConstant;
import com.light.core.utils.email.MailUtils;
import com.light.core.utils.ip.AddressUtils;
import com.qixidi.auth.domain.dto.SysBlackListDto;
import com.qixidi.auth.manager.SysBlackListManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局接口防刷拦截实现
 */
@Slf4j
public class CurrentLimitingHandler {

    private final ReentrantLock Lock = new ReentrantLock();

    /**
     * 免防刷 IP 白名单：本机回环地址
     * SSR 渲染服务的请求全部来自 127.0.0.1，若参与计数等于把全站服务端流量算进一个桶，
     * 高峰期触发拉黑后本机请求全部被拦截（返回空响应），SSR 整体瘫痪（2026-10-08 生产实例）
     */
    private static final Set<String> SKIP_LIMIT_IPS = Set.of("127.0.0.1", "0:0:0:0:0:0:0:1", "::1");

    public Boolean currentLimiting(HttpServletRequest request) {
        String ip = AddressUtils.gainIp(request);
        // 本机内部调用（SSR 渲染、运维 curl）不参与防刷计数
        if (SKIP_LIMIT_IPS.contains(ip)) {
            return true;
        }
        Lock.lock();
        try {
            if (SysBlackListManager.inst().isIp(ip)) {
                throw new Exception("当前IP：[" + ip + "]，检测到非法操作，已被加入黑名单");
            }
            com.qixidi.auth.domain.dto.SysBlackListDto blackListMap = SysBlackListManager.inst().getBlackListMap(ip);
            Long maxFrequency = SysBlackListManager.inst().getMaxFrequency();
            String url = request.getRequestURL().toString();
            //计数
            List<String> urlList = blackListMap.getUrlList();
            urlList.add(url);
            if (blackListMap.getIpLisSize() >= maxFrequency) {
                //保存黑名单
                SysBlackListManager.inst().addIp(ip, blackListMap);
                //                发送邮件
                MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "新增黑名单", "已封禁IP：" + ip + "IP归属地：" + AddressUtils.getRealAddressByIP(ip)
                        + "；请求接口：" + url + " 请求方式：" + request.getMethod());
                throw new Exception("当前IP：[" + ip + "]，检测到非法操作，已被加入黑名单");
            } else {
                if (blackListMap.getTimeInterval() >= SysBlackListManager.inst().getTimeFrequency()) {//刷新计数
                    SysBlackListManager.inst().addBlackListMap(ip, new SysBlackListDto(System.currentTimeMillis()));
                } else {
                    //累加
                    SysBlackListManager.inst().addBlackListMap(ip, blackListMap);
                }
            }
        } catch (Exception e) {
            log.error("防刷拦截处理异常：{}", e.getMessage(), e);
            return false;
        } finally {
            Lock.unlock();
        }
        return true;
    }
}

