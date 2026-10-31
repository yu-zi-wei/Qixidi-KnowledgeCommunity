package com.qixidi.system.api.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.configure.ToNavigationBo;
import com.qixidi.business.domain.vo.configure.ToNavigationVo;
import com.qixidi.business.service.configure.IToNavigationService;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 导航栏配置管理
 *
 * @author aurora
 * @date 2022-09-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/configure/navigation")
@LogModule("导航栏配置")
public class ToNavigationController {

    private final IToNavigationService iToNavigationService;

    /**
     * 查询导航栏配置列表
     */
    @PostMapping("/list")
    public TableDataInfo<ToNavigationVo> list(@Validated(QueryGroup.class) @RequestBody ToNavigationBo bo) {
        return iToNavigationService.queryPageListAdmin(bo, bo);
    }

    /**
     * 导出导航栏配置列表
     */
    @SaCheckPermission("navigation:export")
    @Log(title = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToNavigationBo bo, HttpServletResponse response) {
        List<ToNavigationVo> list = iToNavigationService.queryList(bo);
        ExcelUtil.exportExcel(list, "导航栏配置", ToNavigationVo.class, response);
    }

    /**
     * 获取导航栏配置详细信息
     */
    @SaCheckPermission("navigation:query")
    @GetMapping("/{id}")
    public ToNavigationVo getInfo(@PathVariable("id") Long id) {
        return iToNavigationService.queryById(id);
    }

    /**
     * 新增导航栏配置
     */
    @SaCheckPermission("navigation:add")
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody ToNavigationBo bo) {
        iToNavigationService.insertByBo(bo);
    }

    /**
     * 修改导航栏配置
     */
    @SaCheckPermission("navigation:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody ToNavigationBo bo) {
        iToNavigationService.updateByBo(bo);
    }

    /**
     * 删除导航栏配置
     */
    @SaCheckPermission("navigation:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iToNavigationService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

