package com.aurora.web.controller.frontDesk.configure;

import com.aurora.business.domain.bo.configure.ToNavigationBo;
import com.aurora.business.domain.bo.configure.ToSidebarBo;
import com.aurora.business.domain.vo.configure.ToNavigationVo;
import com.aurora.business.domain.vo.configure.ToSidebarVo;
import com.aurora.business.service.configure.IToNavigationService;
import com.aurora.business.service.configure.IToSidebarService;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.QueryGroup;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 导航栏配置Controller
 *
 * @author aurora
 * @date 2022-09-16
 */
@Validated
@Api(value = "导航栏配置控制器", tags = {"导航栏配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/configure")
public class ToNavigationApiController extends BaseController {

    private final IToNavigationService iToNavigationService;

    private final IToSidebarService iToSidebarService;

    /**
     * 查询导航栏配置列表
     */
    @ApiOperation("顶部导航栏配置列表")
    @GetMapping("/navigation/list")
    public TableDataInfo<ToNavigationVo> list(@Validated(QueryGroup.class) ToNavigationBo bo, PageQuery pageQuery) {
        return iToNavigationService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询侧边栏配置列表
     */
    @ApiOperation("侧边栏配置列表")
    @GetMapping("/sidebar/list")
    public TableDataInfo<ToSidebarVo> list(@Validated(QueryGroup.class) ToSidebarBo bo, PageQuery pageQuery) {
        return iToSidebarService.queryPageList(bo, pageQuery);
    }

}

