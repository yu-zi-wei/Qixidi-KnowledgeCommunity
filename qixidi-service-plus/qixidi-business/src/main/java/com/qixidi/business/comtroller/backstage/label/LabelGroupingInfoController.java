package com.qixidi.business.comtroller.backstage.label;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.label.LabelGroupingInfoBo;
import com.qixidi.business.domain.vo.label.LabelGroupingInfoVo;
import com.qixidi.business.service.label.ILabelGroupingInfoService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
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
 * 标签分组信息管理
 *
 * @author aurora
 * @date 2022-08-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/groupingInfo")
public class LabelGroupingInfoController extends BaseController {

    private final ILabelGroupingInfoService iLabelGroupingInfoService;

    /**
     * 查询标签分组信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<LabelGroupingInfoVo> list(@Validated(QueryGroup.class) LabelGroupingInfoBo bo, PageQuery pageQuery) {
        return iLabelGroupingInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出标签分组信息列表
     */
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
    @SaCheckPermission("system:groupingInfo:query")
    @GetMapping("/{id}")
    public R<LabelGroupingInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iLabelGroupingInfoService.queryById(id));
    }

    /**
     * 新增标签分组信息
     */
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
    @SaCheckPermission("system:groupingInfo:remove")
    @Log(title = "标签分组信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iLabelGroupingInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

