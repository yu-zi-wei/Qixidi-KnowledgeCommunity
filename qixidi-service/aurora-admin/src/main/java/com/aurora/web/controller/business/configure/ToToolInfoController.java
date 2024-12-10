package com.aurora.web.controller.business.configure;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.configure.ToToolInfoBo;
import com.aurora.business.domain.vo.configure.ToToolInfoVo;
import com.aurora.business.service.configure.IToToolInfoService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 工具信息管理
 *
 * @author aurora
 * @date 2022-10-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/toolInfo")
public class ToToolInfoController extends BaseController {

    private final IToToolInfoService iToToolInfoService;

    /**
     * 查询工具信息列表
     */
    @SaCheckPermission("business:toolInfo:list")
    @GetMapping("/list")
    public R list(@Validated(QueryGroup.class) ToToolInfoBo bo, PageQuery pageQuery) {
        return R.ok(iToToolInfoService.queryPageList(bo, pageQuery));
    }

    /**
     * 导出工具信息列表
     */
    @ApiOperation("导出具信息列表")
    @SaCheckPermission("business:toolInfo:export")
    @Log(title = "工具信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ToToolInfoBo bo, HttpServletResponse response) {
        List<ToToolInfoVo> list = iToToolInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "工具信息", ToToolInfoVo.class, response);
    }

    /**
     * 获取工具信息详细信息
     */
    @SaCheckPermission("business:toolInfo:query")
    @GetMapping("/{id}")
    public R<ToToolInfoVo> getInfo(@ApiParam("主键")
                                   @NotNull(message = "主键不能为空")
                                   @PathVariable("id") Long id) {
        return R.ok(iToToolInfoService.queryById(id));
    }

    /**
     * 新增工具信息
     */
    @SaCheckPermission("business:toolInfo:add")
    @Log(title = "工具信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ToToolInfoBo bo) {
        return toAjax(iToToolInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改工具信息
     */
    @SaCheckPermission("business:toolInfo:edit")
    @Log(title = "工具信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ToToolInfoBo bo) {
        return toAjax(iToToolInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除工具信息
     */
    @SaCheckPermission("business:toolInfo:remove")
    @Log(title = "工具信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iToToolInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
