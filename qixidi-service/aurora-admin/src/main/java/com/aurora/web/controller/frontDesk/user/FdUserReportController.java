package com.aurora.web.controller.frontDesk.user;

import com.aurora.business.domain.bo.user.UserReportBo;
import com.aurora.business.service.user.IUserReportService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Validated
@Api(value = "用户签到控制器", tags = {"用户签到管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/user/report")
public class FdUserReportController extends BaseController {

    private final IUserReportService iUserReportService;

    /**
     * 查询用户签到列表
     */
    @ApiOperation("查询用户签到列表")
    @GetMapping("/list")
    public R<Map<String, Object>> list() {
        return R.ok(iUserReportService.List());
    }

    /**
     * 新增用户签到
     */
    @ApiOperation("新增用户签到")
    @Log(title = "用户签到", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserReportBo bo) throws Exception {
        return toAjax(iUserReportService.insertByBo(bo) ? 1 : 0);
    }
}
