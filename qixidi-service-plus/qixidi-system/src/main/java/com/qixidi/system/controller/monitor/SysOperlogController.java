package com.qixidi.system.controller.monitor;

import cn.dev33.satoken.annotation.SaCheckPermission;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.controller.BaseController;
import com.qixidi.system.domain.entity.SysOperLog;
import com.qixidi.system.service.ISysOperLogService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {

    private final ISysOperLogService operLogService;

    /**
     * 查询操作日志记录列表
     *
     * @param operLog
     * @param pageQuery
     * @return
     */
    @SaCheckPermission("monitor:operlog:list")
    @GetMapping("/list")
    public TableDataInfo<SysOperLog> list(SysOperLog operLog, PageQuery pageQuery) {
        return operLogService.selectPageOperLogList(operLog, pageQuery);
    }

    /**
     * 导出操作日志记录列表
     *
     * @param operLog
     * @param response
     */
    @Log(title = "操作日志", businessType = BusinessType.EXPORT)
    @SaCheckPermission("monitor:operlog:export")
    @PostMapping("/export")
    public void export(SysOperLog operLog, HttpServletResponse response) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil.exportExcel(list, "操作日志", SysOperLog.class, response);
    }

    /**
     * 删除操作日志记录
     *
     * @param operIds
     * @return
     */
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/{operIds}")
    public R<Void> remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    /**
     * 清空操作日志记录
     *
     * @return
     */
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/clean")
    public R<Void> clean() {
        operLogService.cleanOperLog();
        return R.ok();
    }
}
