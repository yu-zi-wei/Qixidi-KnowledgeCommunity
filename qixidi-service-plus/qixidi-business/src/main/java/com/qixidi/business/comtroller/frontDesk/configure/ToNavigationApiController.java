package com.qixidi.business.comtroller.frontDesk.configure;

import com.qixidi.business.domain.bo.configure.ToNavigationBo;
import com.qixidi.business.domain.vo.configure.ToNavigationVo;
import com.qixidi.business.service.configure.IToNavigationService;
import com.qixidi.business.service.configure.IToSidebarService;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 【前台】导航栏配置管理
 *
 * @author aurora
 * @date 2022-09-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/configure")
public class ToNavigationApiController extends BaseController {

    private final IToNavigationService iToNavigationService;

    private final IToSidebarService iToSidebarService;

    /**
     * 查询导航栏配置列表
     */
    @GetMapping("/navigation/list")
    public TableDataInfo<ToNavigationVo> list(@Validated(QueryGroup.class) ToNavigationBo bo, PageQuery pageQuery) {
        return iToNavigationService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询侧边栏配置列表
     */
    @GetMapping("/sidebar/list")
    public R sidebarList(@Validated(QueryGroup.class) ToNavigationBo bo) {
        return R.ok(iToSidebarService.sidebarList(bo));
    }
}

