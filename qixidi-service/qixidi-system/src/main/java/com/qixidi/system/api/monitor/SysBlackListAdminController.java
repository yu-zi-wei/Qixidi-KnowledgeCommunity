package com.qixidi.system.api.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.core.utils.StringUtils;
import com.light.core.utils.ip.AddressUtils;
import com.light.exception.ServiceException;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.auth.domain.bo.SysBlackListBo;
import com.qixidi.auth.domain.entity.SysBlackList;
import com.qixidi.auth.manager.SysBlackListManager;
import com.qixidi.auth.service.SysBlackListService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

/**
 * IP 黑名单管理（防刷限流拉黑记录 sys_black_list）
 *
 * 增删后调 SysBlackListManager.loadData() 重建内存黑名单，拦截即时生效
 *
 * @author ziwei
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/black")
@LogModule("IP黑名单")
public class SysBlackListAdminController {

    private final SysBlackListService blackListService;

    /**
     * 分页查询黑名单
     */
    @SaCheckPermission("blackList:list")
    @PostMapping("/list")
    public TableDataInfo<SysBlackList> list(@RequestBody SysBlackListBo bo) {
        return blackListService.queryPageList(bo, bo);
    }

    /**
     * 手动加黑（ip 与 uid 至少一项；归属地按 ip 反查）
     */
    @SaCheckPermission("blackList:add")
    @Log(title = "加黑IP", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    public void add(@RequestBody SysBlackListBo bo) {
        if (StringUtils.isBlank(bo.getIp()) && StringUtils.isBlank(bo.getUid())) {
            throw new ServiceException("IP 与 UID 至少填写一项");
        }
        SysBlackList black = new SysBlackList();
        black.setIp(bo.getIp());
        black.setUid(bo.getUid());
        black.setDetail(bo.getDetail());
        black.setHomePlace(StringUtils.isNotBlank(bo.getIp()) ? AddressUtils.getRealAddressByIP(bo.getIp()) : null);
        black.setCreateTime(new Date());
        blackListService.insertBlack(black);
        SysBlackListManager.inst().loadData();
    }

    /**
     * 批量移出黑名单
     */
    @SaCheckPermission("blackList:remove")
    @Log(title = "移出IP黑名单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        if (blackListService.deleteByIds(Arrays.asList(ids)) <= 0) {
            throw new ServiceException("操作失败");
        }
        SysBlackListManager.inst().loadData();
    }
}
