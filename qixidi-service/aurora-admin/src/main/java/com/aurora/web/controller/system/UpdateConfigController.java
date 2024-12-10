package com.aurora.web.controller.system;

import com.aurora.common.core.domain.R;
import com.aurora.framework.manager.SysBlackListManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 黑名单管理
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/update/config")
public class UpdateConfigController {

    /**
     * 更新黑名单缓存
     *
     * @return
     */
    @GetMapping("/black-list")
    public R list() {
        SysBlackListManager.inst().loadData();
        return R.ok();
    }
}
