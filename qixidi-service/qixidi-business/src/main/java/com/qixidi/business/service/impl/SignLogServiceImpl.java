package com.qixidi.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.ServletUtils;
import com.light.core.utils.StringUtils;
import com.light.core.utils.ip.AddressUtils;
import com.qixidi.business.domain.bo.user.SignLogBo;
import com.qixidi.business.domain.entity.user.SignLog;
import com.qixidi.business.mapper.user.SignLogMapper;
import com.qixidi.business.service.ISignLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * 前台登录日志 Service 实现
 *
 * @author ziwei
 * @date 2026-10-31
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SignLogServiceImpl implements ISignLogService {

    @Mapper
    private final SignLogMapper baseMapper;

    @Override
    public void recordSignLog(String uid, String userName, boolean logout) {
        try {
            HttpServletRequest request = ServletUtils.getRequest();
            String ip = request == null ? null : ServletUtils.getClientIP(request);
            SignLog signLog = new SignLog();
            signLog.setUid(uid);
            // user_name 列 NOT NULL：第三方登录 username 可能为空，兜底用 uid
            signLog.setUserName(StringUtils.isNotBlank(userName) ? userName : uid);
            signLog.setIpAddress(ip);
            signLog.setLocation(AddressUtils.getRealAddressByIP(ip));
            signLog.setType(logout ? 1 : 0);
            signLog.setCreateTime(new Date());
            baseMapper.insert(signLog);
        } catch (Exception e) {
            log.error("记录前台登录日志失败：uid={}, userName={}, logout={}", uid, userName, logout, e);
        }
    }

    @Override
    public TableDataInfo<SignLog> queryPageList(SignLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SignLog> lqw = new LambdaQueryWrapper<SignLog>()
            .like(StringUtils.isNotBlank(bo.getUserName()), SignLog::getUserName, bo.getUserName())
            .like(StringUtils.isNotBlank(bo.getIpAddress()), SignLog::getIpAddress, bo.getIpAddress())
            .eq(bo.getType() != null, SignLog::getType, bo.getType())
            .orderByDesc(SignLog::getId);
        Page<SignLog> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    @Override
    public void deleteSignLogByIds(Long[] ids) {
        baseMapper.deleteBatchIds(Arrays.asList(ids));
    }

    @Override
    public void cleanSignLog() {
        baseMapper.cleanTable();
    }

}
