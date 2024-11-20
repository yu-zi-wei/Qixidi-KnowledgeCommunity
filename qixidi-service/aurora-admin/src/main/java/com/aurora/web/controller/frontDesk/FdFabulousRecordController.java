package com.aurora.web.controller.frontDesk;

import com.aurora.business.domain.bo.fabulous.FabulousRecordBo;
import com.aurora.business.domain.bo.user.UserHomeBo;
import com.aurora.business.service.fabulous.IFabulousRecordService;
import com.aurora.common.annotation.Log;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Api(value = "前台点赞控制器", tags = {"点赞管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/fabulous")
public class FdFabulousRecordController extends BaseController {

    private final IFabulousRecordService iFabulousRecordService;

    /**
     * 点赞
     */
    @ApiOperation("点赞")
    @Log(title = "点赞", businessType = BusinessType.INSERT)
    @PostMapping("/spot")
    public R spotFabulous(@Validated(AddGroup.class) @RequestBody FabulousRecordBo bo) {
        return iFabulousRecordService.spotFabulous(bo);
    }

    /**
     * 取消点赞
     */
    @ApiOperation("取消点赞")
    @Log(title = "取消点赞", businessType = BusinessType.INSERT)
    @PostMapping("/cancel")
    public R<Void> cancelFabulous(@Validated(AddGroup.class) @RequestBody FabulousRecordBo bo) {
        return iFabulousRecordService.cancelFabulous(bo);
    }

    /**
     * 用户点赞列表
     */
    @ApiOperation("用户点赞列表")
    @GetMapping("/fabulous/list")
    public R fabulousList(@Validated(AddGroup.class) UserHomeBo bo, PageQuery pageQuery) {
        return R.ok(iFabulousRecordService.fabulousList(bo, pageQuery));
    }

}
