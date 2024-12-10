package com.aurora.web.controller.business.label;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.label.LabelInfoBo;
import com.aurora.business.domain.vo.label.LabelInfoVo;
import com.aurora.business.service.label.ILabelInfoService;
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
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 标签信息Controller
 *
 * @author aurora
 * @date 2022-08-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/label-info")
public class LabelInfoController extends BaseController {

    private final ILabelInfoService iLabelInfoService;

    /**
     * 查询标签信息列表
     */
    @GetMapping("/list")
    public TableDataInfo<LabelInfoVo> list(@Validated(QueryGroup.class) LabelInfoBo bo, PageQuery pageQuery) {
        return iLabelInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出标签信息列表
     */
    @SaCheckPermission("business:label-info:export")
    @Log(title = "标签信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated LabelInfoBo bo, HttpServletResponse response) {
        List<LabelInfoVo> list = iLabelInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "标签信息", LabelInfoVo.class, response);
    }

    /**
     * 获取标签信息详细信息
     */
    @SaCheckPermission("business:label-info:query")
    @GetMapping("/{id}")
    public R<LabelInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iLabelInfoService.queryById(id));
    }

    /**
     * 新增标签信息
     */
    @SaCheckPermission("business:label-info:add")
    @Log(title = "标签信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody LabelInfoBo bo) {
        return toAjax(iLabelInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改标签信息
     */
    @SaCheckPermission("business:label-info:edit")
    @Log(title = "标签信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody LabelInfoBo bo) {
        return toAjax(iLabelInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除标签信息
     */
    @SaCheckPermission("system:info:remove")
    @Log(title = "标签信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iLabelInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

