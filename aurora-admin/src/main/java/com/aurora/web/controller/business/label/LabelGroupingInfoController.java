package com.aurora.web.controller.business.label;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.label.LabelGroupingInfoBo;
import com.aurora.business.domain.vo.label.LabelGroupingInfoVo;
import com.aurora.business.service.label.ILabelGroupingInfoService;
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
 * 标签分组信息Controller
 *
 * @author aurora
 * @date 2022-08-16
 */
@Validated
@Api(value = "标签分组信息控制器", tags = {"标签分组信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/groupingInfo")
public class LabelGroupingInfoController extends BaseController {

    private final ILabelGroupingInfoService iLabelGroupingInfoService;

    /**
     * 查询标签分组信息列表
     */
    @ApiOperation("查询标签分组信息列表")
    @GetMapping("/list")
    public TableDataInfo<LabelGroupingInfoVo> list(@Validated(QueryGroup.class) LabelGroupingInfoBo bo, PageQuery pageQuery) {
        return iLabelGroupingInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出标签分组信息列表
     */
    @ApiOperation("导出标签分组信息列表")
    @SaCheckPermission("system:groupingInfo:export")
    @Log(title = "标签分组信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated LabelGroupingInfoBo bo, HttpServletResponse response) {
        List<LabelGroupingInfoVo> list = iLabelGroupingInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "标签分组信息", LabelGroupingInfoVo.class, response);
    }

    /**
     * 获取标签分组信息详细信息
     */
    @ApiOperation("获取标签分组信息详细信息")
    @SaCheckPermission("system:groupingInfo:query")
    @GetMapping("/{id}")
    public R<LabelGroupingInfoVo> getInfo(@ApiParam("主键")
                                          @NotNull(message = "主键不能为空")
                                          @PathVariable("id") Long id) {
        return R.ok(iLabelGroupingInfoService.queryById(id));
    }

    /**
     * 新增标签分组信息
     */
    @ApiOperation("新增标签分组信息")
    @SaCheckPermission("system:groupingInfo:add")
    @Log(title = "标签分组信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LabelGroupingInfoBo bo) {
        return toAjax(iLabelGroupingInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改标签分组信息
     */
    @ApiOperation("修改标签分组信息")
    @SaCheckPermission("system:groupingInfo:edit")
    @Log(title = "标签分组信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LabelGroupingInfoBo bo) {
        return toAjax(iLabelGroupingInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除标签分组信息
     */
    @ApiOperation("删除标签分组信息")
    @SaCheckPermission("system:groupingInfo:remove")
    @Log(title = "标签分组信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iLabelGroupingInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

