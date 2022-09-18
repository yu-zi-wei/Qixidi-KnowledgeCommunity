package com.aurora.web.controller.business.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.configure.ToSidebarBo;
import com.aurora.business.domain.vo.configure.ToSidebarVo;
import com.aurora.business.service.configure.IToSidebarService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 侧边栏配置Controller
 *
 * @author aurora
 * @date 2022-09-16
 */
@Validated
@Api(value = "侧边栏配置控制器", tags = {"侧边栏配置管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/configure/sidebar")
public class ToSidebarController extends BaseController {

    private final IToSidebarService iToSidebarService;

    /**
     * 查询侧边栏配置列表
     */
    @ApiOperation("查询侧边栏配置列表")
    @GetMapping("/list")
    public TableDataInfo<ToSidebarVo> list(@Validated(QueryGroup.class) ToSidebarBo bo, PageQuery pageQuery) {
        return iToSidebarService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出侧边栏配置列表
     */
    @ApiOperation("导出侧边栏配置列表")
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
    @ApiOperation("获取侧边栏配置详细信息")
    @SaCheckPermission("business:sidebar:query")
    @GetMapping("/{id}")
    public R<ToSidebarVo> getInfo(@ApiParam("主键")
                                  @NotNull(message = "主键不能为空")
                                  @PathVariable("id") Long id) {
        return R.ok(iToSidebarService.queryById(id));
    }

    /**
     * 新增侧边栏配置
     */
    @ApiOperation("新增侧边栏配置")
    @SaCheckPermission("business:sidebar:add")
    @Log(title = "侧边栏配置", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ToSidebarBo bo) {
        return toAjax(iToSidebarService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改侧边栏配置
     */
    @ApiOperation("修改侧边栏配置")
    @SaCheckPermission("business:sidebar:edit")
    @Log(title = "侧边栏配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ToSidebarBo bo) {
        return toAjax(iToSidebarService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除侧边栏配置
     */
    @ApiOperation("删除侧边栏配置")
    @SaCheckPermission("business:sidebar:remove")
    @Log(title = "侧边栏配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iToSidebarService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

