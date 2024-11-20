package com.aurora.web.controller.business;

import com.aurora.business.service.configure.IToNavigationService;
import com.aurora.business.service.configure.IToSidebarService;
import com.aurora.business.service.shield.IToShieldWordService;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.domain.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 刷新缓存
 */
@Api(value = "刷新缓存控制器", tags = {"刷新缓存馈管理"})
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/business/cache/refresh")
public class cacheRefreshController {
    private final IToNavigationService iToNavigationService;
    private final IToSidebarService iToSidebarService;
    private final IToShieldWordService iToShieldWordService;

    @GetMapping("/navigation")
    @RepeatSubmit()
    @ApiOperation("刷新导航栏配置")
    public R cacheNavigation() {
        iToNavigationService.queryPageList();
        log.info("刷新导航栏配置成功");
        return R.ok();
    }

    @GetMapping("/sidebar")
    @RepeatSubmit()
    @ApiOperation("刷新侧边栏配置")
    public R cacheSidebar() {
        iToSidebarService.sidebarList();
        log.info("刷新侧边栏配置成功");
        return R.ok();
    }

    @GetMapping("/blocking-words")
    @RepeatSubmit()
    @ApiOperation("刷新屏蔽词缓存")
    public R cacheBlockingWords() {
        iToShieldWordService.ShieldWordRefresh();
        log.info("刷新屏蔽词缓存成功");
        return R.ok();
    }

    @GetMapping("/blocking-words/text/{text}")
    @RepeatSubmit()
    @ApiOperation("测试屏蔽词")
    public R cacheBlockingWordsText(@PathVariable String text) {
        Map<String, Object> detection = iToShieldWordService.detection(text);
        log.info("测试屏蔽词");
        return R.ok(detection);
    }


}
