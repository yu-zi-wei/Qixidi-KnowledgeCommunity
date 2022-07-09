package com.aurora.web.controller.business.label;
import java.util.List;
import java.util.Arrays;

import com.aurora.business.domain.bo.label.LabelInfoBo;
import com.aurora.business.domain.vo.label.LabelInfoVo;
import com.aurora.business.service.label.ILabelInfoService;
import lombok.RequiredArgsConstructor;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.annotation.Log;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import com.aurora.common.core.page.TableDataInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiOperation;

/**
 * 标签信息Controller
 *
 * @author ruoyi
 * @date 2022-07-09
 */
@Validated
@Api(value = "标签信息控制器", tags = {"标签信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/info")
public class LabelInfoController extends BaseController {

    private final ILabelInfoService iLabelInfoService;

    /**
     * 查询标签信息列表
     */
    @ApiOperation("查询标签信息列表")
    @GetMapping("/list")
    public TableDataInfo<LabelInfoVo> list(@Validated(QueryGroup.class) LabelInfoBo bo, PageQuery pageQuery) {
        return iLabelInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出标签信息列表
     */
    @ApiOperation("导出标签信息列表")
    @Log(title = "标签信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated LabelInfoBo bo, HttpServletResponse response) {
        List<LabelInfoVo> list = iLabelInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "标签信息", LabelInfoVo.class, response);
    }

    /**
     * 获取标签信息详细信息
     */
    @ApiOperation("获取标签信息详细信息")
    @GetMapping("/{id}")
    public R<LabelInfoVo> getInfo(@ApiParam("主键")
                                  @NotNull(message = "主键不能为空")
                                  @PathVariable("id") Long id) {
        return R.ok(iLabelInfoService.queryById(id));
    }

    /**
     * 新增标签信息
     */
    @ApiOperation("新增标签信息")
    @Log(title = "标签信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LabelInfoBo bo) {
        return toAjax(iLabelInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改标签信息
     */
    @ApiOperation("修改标签信息")
    @Log(title = "标签信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LabelInfoBo bo) {
        return toAjax(iLabelInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除标签信息
     */
    @ApiOperation("删除标签信息")
    @Log(title = "标签信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iLabelInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

