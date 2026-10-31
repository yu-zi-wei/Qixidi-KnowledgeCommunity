package com.qixidi.system.api.label;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.label.LabelGroupingInfoBo;
import com.qixidi.business.domain.bo.label.LabelMergeBo;
import com.qixidi.business.domain.vo.label.LabelGroupingInfoVo;
import com.qixidi.business.service.label.ILabelGroupingInfoService;
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
 * 标签分组信息管理
 *
 * @author aurora
 * @date 2022-08-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/groupingInfo")
@LogModule("标签分组信息")
public class LabelGroupingInfoController {

    private final ILabelGroupingInfoService iLabelGroupingInfoService;

    /**
     * 查询标签分组信息列表
     */
    @PostMapping("/list")
    public TableDataInfo<LabelGroupingInfoVo> list(@Validated(QueryGroup.class) @RequestBody LabelGroupingInfoBo bo) {
        return iLabelGroupingInfoService.queryPageList(bo, bo);
    }

    /**
     * 导出标签分组信息列表
     */
    @SaCheckPermission("groupingInfo:export")
    @Log(title = "导出", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated LabelGroupingInfoBo bo, HttpServletResponse response) {
        List<LabelGroupingInfoVo> list = iLabelGroupingInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "标签分组信息", LabelGroupingInfoVo.class, response);
    }

    /**
     * 获取标签分组信息详细信息
     */
    @SaCheckPermission("groupingInfo:query")
    @GetMapping("/{id}")
    public LabelGroupingInfoVo getInfo(@PathVariable("id") Long id) {
        return iLabelGroupingInfoService.queryById(id);
    }

    /**
     * 新增标签分组信息
     */
    @SaCheckPermission("groupingInfo:add")
    @Log(title = "新增", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody LabelGroupingInfoBo bo) {
        iLabelGroupingInfoService.insertByBo(bo);
    }

    /**
     * 修改标签分组信息
     */
    @SaCheckPermission("groupingInfo:edit")
    @Log(title = "修改", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody LabelGroupingInfoBo bo) {
        iLabelGroupingInfoService.updateByBo(bo);
    }

    /**
     * 合并标签分组：源分组下的标签与文章归属迁移至目标分组后删除源分组
     */
    @SaCheckPermission("groupingInfo:merge")
    @Log(title = "合并", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/merge")
    public void merge(@Validated @RequestBody LabelMergeBo bo) {
        iLabelGroupingInfoService.mergeGrouping(bo.getSourceId(), bo.getTargetId());
    }

    /**
     * 删除标签分组信息
     */
    @SaCheckPermission("groupingInfo:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iLabelGroupingInfoService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

