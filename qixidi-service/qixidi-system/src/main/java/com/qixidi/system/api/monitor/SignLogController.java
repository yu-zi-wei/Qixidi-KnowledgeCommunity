package com.qixidi.system.api.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;

import com.qixidi.business.domain.bo.user.SignLogBo;
import com.qixidi.business.domain.entity.user.SignLog;
import com.qixidi.business.service.ISignLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 前台登录日志管理（b_sign_log）
 *
 * @author ziwei
 * @date 2026-10-31
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/signLog")
@LogModule("前台登录日志")
public class SignLogController {

    private final ISignLogService signLogService;

    /**
     * 分页查询前台登录日志
     */
    @SaCheckPermission("monitor:signLog:list")
    @PostMapping("/list")
    public TableDataInfo<SignLog> list(@RequestBody SignLogBo bo) {
        return signLogService.queryPageList(bo, bo);
    }

    /**
     * 删除前台登录日志
     */
    @SaCheckPermission("monitor:signLog:remove")
    @Log(title = "删除前台登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        signLogService.deleteSignLogByIds(ids);
    }

    /**
     * 清空前台登录日志
     */
    @SaCheckPermission("monitor:signLog:remove")
    @Log(title = "清空前台登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public void clean() {
        signLogService.cleanSignLog();
    }
}
