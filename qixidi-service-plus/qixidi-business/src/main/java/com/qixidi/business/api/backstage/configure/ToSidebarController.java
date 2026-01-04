package com.qixidi.business.api.backstage.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;

import com.qixidi.business.domain.bo.configure.ToSidebarBo;
import com.qixidi.business.domain.vo.configure.ToSidebarVo;
import com.qixidi.business.service.configure.IToSidebarService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * 侧边栏配置管理
 *
 * @author aurora
 * @date 2022-09-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/configure/sidebar")
public class ToSidebarController {

    private final IToSidebarService iToSidebarService;

    /**
     * 查询侧边栏配置列表
     */
    @GetMapping("/list")
    public TableDataInfo<ToSidebarVo> list(@Validated(QueryGroup.class) ToSidebarBo bo, PageQuery pageQuery) {
        return iToSidebarService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出侧边栏配置列表
     */
    @SaCheckPermission("business:sidebar:export")
    @Log(title = "侧边栏配置", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToSidebarBo bo, HttpServletResponse response) {
        List<ToSidebarVo> list = iToSidebarService.queryList(bo);
        ExcelUtil.exportExcel(list, "侧边栏配置", ToSidebarVo.class, response);
    }

    /**
     * 获取侧边栏配置详细信息
     */
    @SaCheckPermission("business:sidebar:query")
    @GetMapping("/{id}")
    public ToSidebarVo getInfo(@PathVariable("id") Long id) {
        return iToSidebarService.queryById(id);
    }

    /**
     * 新增侧边栏配置
     */
    @SaCheckPermission("business:sidebar:add")
    @Log(title = "侧边栏配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody ToSidebarBo bo) {
        iToSidebarService.insertByBo(bo);
    }

    /**
     * 修改侧边栏配置
     */
    @SaCheckPermission("business:sidebar:edit")
    @Log(title = "侧边栏配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody ToSidebarBo bo) {
        iToSidebarService.updateByBo(bo);
    }

    /**
     * 删除侧边栏配置
     */
    @SaCheckPermission("business:sidebar:remove")
    @Log(title = "侧边栏配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iToSidebarService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

