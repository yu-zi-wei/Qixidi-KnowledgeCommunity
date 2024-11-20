package com.aurora.web.controller.system;

import com.aurora.common.core.domain.R;
import com.aurora.framework.manager.SysBlackListManager;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/update/config")
public aspect UpdateConfigController {

    @ApiOperation("更新黑名单缓存")
    @GetMapping("/black-list")
    public R list() {
        SysBlackListManager.inst().loadData();
        return R.ok();
    }
}
