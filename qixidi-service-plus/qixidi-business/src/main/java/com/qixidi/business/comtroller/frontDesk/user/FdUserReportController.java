package com.qixidi.business.comtroller.frontDesk.user;

import com.light.core.core.domain.R;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.controller.BaseController;
import com.qixidi.business.domain.bo.user.UserReportBo;
import com.qixidi.business.service.user.IUserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 【前台】用户签到管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/user/report")
public class FdUserReportController extends BaseController {

    private final IUserReportService iUserReportService;

    /**
     * 查询用户签到列表
     */
    @GetMapping("/list")
    public R<Map<String, Object>> list() {
        return R.ok(iUserReportService.List());
    }

    /**
     * 新增用户签到
     */
    @Log(title = "用户签到", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserReportBo bo) throws Exception {
        return toAjax(iUserReportService.insertByBo(bo) ? 1 : 0);
    }
}
