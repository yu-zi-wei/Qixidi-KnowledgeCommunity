package com.qixidi.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.auth.annotation.Log;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.controller.BaseController;
import com.qixidi.system.domain.entity.SysNotice;
import com.qixidi.system.service.ISysNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 公告信息管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

    private final ISysNoticeService noticeService;

    /**
     * 获取通知公告列表
     */
    @SaCheckPermission("system:notice:list")
    @GetMapping("/list")
    public TableDataInfo<SysNotice> list(SysNotice notice, PageQuery pageQuery) {
        return noticeService.selectPageNoticeList(notice, pageQuery);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @SaCheckPermission("system:notice:query")
    @GetMapping(value = "/{noticeId}")
    public R<SysNotice> getInfo(@PathVariable Long noticeId) {
        return R.ok(noticeService.selectNoticeById(noticeId));
    }

    /**
     * 新增通知公告
     */
    @SaCheckPermission("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysNotice notice) {
        return toAjax(noticeService.insertNotice(notice));
    }

    /**
     * 修改通知公告
     */
    @SaCheckPermission("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysNotice notice) {
        return toAjax(noticeService.updateNotice(notice));
    }

    /**
     * 删除通知公告
     */
    @SaCheckPermission("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    public R<Void> remove(@PathVariable Long[] noticeIds) {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds));
    }
}
