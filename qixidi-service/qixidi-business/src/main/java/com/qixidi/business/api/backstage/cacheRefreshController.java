package com.qixidi.business.api.backstage;

import com.qixidi.business.service.configure.IToNavigationService;
import com.qixidi.business.service.configure.IToSidebarService;
import com.qixidi.business.service.shield.IToShieldWordService;
import com.light.redission.annotation.RepeatSubmit;
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
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/business/cache/refresh")
public class cacheRefreshController {
    private final IToNavigationService iToNavigationService;
    private final IToSidebarService iToSidebarService;
    private final IToShieldWordService iToShieldWordService;

    /**
     * 刷新导航栏配置
     *
     * @return
     */
    @GetMapping("/navigation")
    @RepeatSubmit()
    public void cacheNavigation() {
        iToNavigationService.queryPageList();
        log.info("刷新导航栏配置成功");
    }

    /**
     * 刷新侧边栏配置
     *
     * @return
     */
    @GetMapping("/sidebar")
    @RepeatSubmit()
    public void cacheSidebar() {
        iToSidebarService.sidebarList();
        log.info("刷新侧边栏配置成功");
    }

    /**
     * 刷新屏蔽词缓存
     *
     * @return
     */
    @GetMapping("/blocking-words")
    @RepeatSubmit()
    public void cacheBlockingWords() {
        iToShieldWordService.ShieldWordRefresh();
        log.info("刷新屏蔽词缓存成功");
    }

    /**
     * 测试屏蔽词
     *
     * @param text
     * @return
     */
    @GetMapping("/blocking-words/text/{text}")
    @RepeatSubmit()
    public Map<String, Object> cacheBlockingWordsText(@PathVariable String text) {
        Map<String, Object> detection = iToShieldWordService.detection(text);
        log.info("测试屏蔽词");
        return detection;
    }


}
