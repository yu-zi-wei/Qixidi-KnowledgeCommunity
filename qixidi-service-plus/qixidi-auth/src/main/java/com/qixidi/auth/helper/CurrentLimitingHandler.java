package com.qixidi.auth.helper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局接口防刷拦截实现
 */
@Slf4j
public class CurrentLimitingHandler {

    private final ReentrantLock Lock = new ReentrantLock();

    public Boolean currentLimiting(HttpServletRequest request) {
        Lock.lock();
//        try {
//            String ip = AddressUtils.gainIp(request);
//            if (SysBlackListManager.inst().isIp(ip)) {
//                throw new Exception("当前IP：[" + ip + "]，检测到非法操作，已被加入黑名单");
//            }
//            SysBlackListDto blackListMap = SysBlackListManager.inst().getBlackListMap(ip);
//            Long maxFrequency = SysBlackListManager.inst().getMaxFrequency();
//            String url = request.getRequestURL().toString();
//            //计数
//            List<String> urlList = blackListMap.getUrlList();
//            urlList.add(url);
//            if (blackListMap.getIpLisSize() >= maxFrequency) {
//                //保存黑名单
//                SysBlackListManager.inst().addIp(ip, blackListMap);
//                //                发送邮件
//                MailUtils.sendText(SystemConstant.AdministratorMailboxList, "新增黑名单", "已封禁IP：" + ip + "IP归属地：" + AddressUtils.getRealAddressByIP(ip)
//                    + "；请求接口：" + url + " 请求方式：" + request.getMethod());
//                throw new Exception("当前IP：[" + ip + "]，检测到非法操作，已被加入黑名单");
//            } else {
//                if (blackListMap.getTimeInterval() >= SysBlackListManager.inst().getTimeFrequency()) {//刷新计数
//                    SysBlackListManager.inst().addBlackListMap(ip, new SysBlackListDto(System.currentTimeMillis()));
//                } else {
//                    //累加
//                    SysBlackListManager.inst().addBlackListMap(ip, blackListMap);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            Lock.unlock();
//        }
        return true;
    }
}

