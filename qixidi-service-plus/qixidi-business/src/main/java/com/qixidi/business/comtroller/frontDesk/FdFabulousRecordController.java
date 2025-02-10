package com.qixidi.business.comtroller.frontDesk;

import com.qixidi.business.domain.bo.fabulous.FabulousRecordBo;
import com.qixidi.business.domain.bo.user.UserHomeBo;
import com.qixidi.business.service.fabulous.IFabulousRecordService;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【前台】点赞管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/fabulous")
public class FdFabulousRecordController extends BaseController {

    private final IFabulousRecordService iFabulousRecordService;

    /**
     * 点赞
     */
    @Log(title = "点赞", businessType = BusinessType.INSERT)
    @PostMapping("/spot")
    public R spotFabulous(@Validated(AddGroup.class) @RequestBody FabulousRecordBo bo) {
        return iFabulousRecordService.spotFabulous(bo);
    }

    /**
     * 取消点赞
     */
    @Log(title = "取消点赞", businessType = BusinessType.INSERT)
    @PostMapping("/cancel")
    public R<Void> cancelFabulous(@Validated(AddGroup.class) @RequestBody FabulousRecordBo bo) {
        return iFabulousRecordService.cancelFabulous(bo);
    }

    /**
     * 用户点赞列表
     */
    @GetMapping("/fabulous/list")
    public R fabulousList(@Validated(AddGroup.class) UserHomeBo bo, PageQuery pageQuery) {
        return R.ok(iFabulousRecordService.fabulousList(bo, pageQuery));
    }

}
