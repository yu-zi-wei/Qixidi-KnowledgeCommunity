package com.qixidi.business.api.backstage.label;

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
import com.qixidi.business.domain.bo.label.LabelInfoBo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.service.label.ILabelInfoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
public class LabelInfoController {

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
    public LabelInfoVo getInfo(@PathVariable("id") Long id) {
        return iLabelInfoService.queryById(id);
    }

    /**
     * 新增标签信息
     */
    @SaCheckPermission("business:label-info:add")
    @Log(title = "标签信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody LabelInfoBo bo) {
        iLabelInfoService.insertByBo(bo);
    }

    /**
     * 修改标签信息
     */
    @SaCheckPermission("business:label-info:edit")
    @Log(title = "标签信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody LabelInfoBo bo) {
        iLabelInfoService.updateByBo(bo);
    }

    /**
     * 删除标签信息
     */
    @SaCheckPermission("system:info:remove")
    @Log(title = "标签信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iLabelInfoService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

