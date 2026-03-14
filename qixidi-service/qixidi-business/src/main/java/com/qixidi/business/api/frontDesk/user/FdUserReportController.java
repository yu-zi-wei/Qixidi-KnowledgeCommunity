package com.qixidi.business.api.frontDesk.user;

import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;

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
public class FdUserReportController {

    private final IUserReportService iUserReportService;

    /**
     * 查询用户签到列表
     */
    @GetMapping("/list")
    public Map<String, Object> list() {
        return iUserReportService.List();
    }

    /**
     * 新增用户签到
     */
    @Log(title = "用户签到", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody UserReportBo bo) throws Exception {
        iUserReportService.insertByBo(bo);
    }
}
