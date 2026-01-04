package com.qixidi.business.api.frontDesk.configure;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;

import com.qixidi.business.domain.bo.configure.ToNavigationBo;
import com.qixidi.business.domain.vo.configure.ToNavigationVo;
import com.qixidi.business.domain.vo.configure.ToSidebarVo;
import com.qixidi.business.service.configure.IToNavigationService;
import com.qixidi.business.service.configure.IToSidebarService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
public class ToNavigationApiController {

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
    public List<ToSidebarVo> sidebarList(@Validated(QueryGroup.class) ToNavigationBo bo) {
        return iToSidebarService.sidebarList(bo);
    }
}

