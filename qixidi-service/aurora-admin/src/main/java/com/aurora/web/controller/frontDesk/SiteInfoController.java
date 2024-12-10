package com.aurora.web.controller.frontDesk;

import com.aurora.business.domain.entity.ToSiteInfo;
import com.aurora.business.domain.vo.FriendLinkVo;
import com.aurora.business.service.SiteInfoService;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【前台-白名单】网站信息管理
 * @author ziwei
 * @date 2024年09月16日
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/site")
public class SiteInfoController {

    @Autowired
    private SiteInfoService siteInfoService;

    /**
     * 网站信息
     * @return
     */
    @GetMapping("/info")
    public R<ToSiteInfo> info() {
        return R.ok(siteInfoService.info());
    }

    /**
     * 友链列表
     * @return
     */
    @GetMapping("/friend-link")
    public TableDataInfo<FriendLinkVo> friendLink() {
        return TableDataInfo.build(siteInfoService.friendLink());
    }
}
