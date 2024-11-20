package com.aurora.framework.manager;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.aurora.common.utils.StringUtils;
import com.aurora.common.utils.ip.AddressUtils;
import com.aurora.common.utils.spring.SpringUtils;
import com.aurora.framework.domain.SysBlackList;
import com.aurora.framework.domain.dto.SysBlackListDto;
import com.aurora.framework.service.SysBlackListService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
@Slf4j
public class SysBlackListManager {
    private static volatile SysBlackListManager _inst;
    private static final Object _lock = new Object();

    public static SysBlackListManager inst() {
        if (_inst == null) {
            synchronized (_lock) {
                if (_inst == null) _inst = new SysBlackListManager();
            }
        }
        return _inst;
    }

    private SysBlackListService sysBlackListService;

    private SysBlackListManager() {
        sysBlackListService = SpringUtils.getBean(SysBlackListService.class);
    }

    //    频率间隔(秒)
    long timeFrequency = 8;
    //    频率(次数)
    long maxFrequency = 250;

    Set<String> blackListUidList = new HashSet<>();
    Set<String> blackListIpList = new HashSet<>();
    //新增黑名单
    ConcurrentHashMap<String, List<String>> newBlackListMap = new ConcurrentHashMap<>();
    ConcurrentHashMap<String, SysBlackListDto> blackListDtoMap = new ConcurrentHashMap<>();

    public void loadData() {
        blackListLokuro();//落库
        List<SysBlackList> sysBlackLists = sysBlackListService.list(null);
        if (CollectionUtil.isEmpty(sysBlackLists)) return;
        Set<String> _blackListUidList = new HashSet<>();
        Set<String> _blackListIpList = new HashSet<>();

        sysBlackLists.forEach(item -> {
            if (StringUtils.isNotEmpty(item.getUid())) {
                _blackListUidList.add(item.getUid());
            }
            if (StringUtils.isNotEmpty(item.getIp())) {
                _blackListIpList.add(item.getIp());
            }
        });
        blackListUidList = _blackListUidList;
        blackListIpList = _blackListIpList;
    }

    public Boolean isUid(String uid) {
        return blackListUidList.contains(uid);
    }

    public void addIp(String ip, SysBlackListDto blackListMap) {
        blackListIpList.add(ip);
        newBlackListMap.put(ip, blackListMap.getUrlList());
    }

    public Boolean isIp(String ip) {
        return blackListIpList.contains(ip);
    }

    public Long getTimeFrequency() {
        return timeFrequency;
    }

    public Long getMaxFrequency() {
        return maxFrequency;
    }

    public SysBlackListDto getBlackListMap(String ip) {
        SysBlackListDto sysBlackListDto = blackListDtoMap.get(ip);
        long time = System.currentTimeMillis();
        if (sysBlackListDto == null) {
            //初始化记录时间
            return new SysBlackListDto(time);
        }
        long stamp = (time - sysBlackListDto.getTimestamp()) / 1000;
        sysBlackListDto.setTimeInterval(stamp);
        sysBlackListDto.setIpLisSize(sysBlackListDto.getUrlList().size());
        return sysBlackListDto;
    }

    public void addBlackListMap(String ip, SysBlackListDto sysBlackListDto) {
        blackListDtoMap.put(ip, sysBlackListDto);
    }

    public void blackListLokuro() {
        if (CollectionUtil.isEmpty(newBlackListMap)) return;
        log.info("黑名单数据落库:{}", newBlackListMap.size());
        List<SysBlackList> list = new ArrayList<>();
        newBlackListMap.forEach((ip, v) -> {
            SysBlackList sysBlackList = new SysBlackList();
            sysBlackList.setIp(ip);
            sysBlackList.setHomePlace(AddressUtils.getRealAddressByIP(ip));
            Map<String, Long> collect = v.stream().collect(Collectors.groupingBy(str -> str, Collectors.counting()));
            sysBlackList.setDetail(JSON.toJSONString(collect));
            sysBlackList.setCreateTime(new Date());
            list.add(sysBlackList);
        });
        sysBlackListService.saveBatch(list);
        //清理黑名单
        newBlackListMap.clear();
    }
}
