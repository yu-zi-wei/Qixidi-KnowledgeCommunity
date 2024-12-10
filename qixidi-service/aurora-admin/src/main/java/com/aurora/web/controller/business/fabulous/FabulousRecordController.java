package com.aurora.web.controller.business.fabulous;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.fabulous.FabulousRecordBo;
import com.aurora.business.domain.vo.fabulous.FabulousRecordVo;
import com.aurora.business.service.fabulous.IFabulousRecordService;
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
 * 点赞信息管理
 *
 * @author aurora
 * @date 2022-10-01
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/fabulous/record")
public class FabulousRecordController extends BaseController {

    private final IFabulousRecordService iFabulousRecordService;

    /**
     * 查询点赞列表
     */
    @SaCheckPermission("fabulous:record:list")
    @GetMapping("/list")
    public TableDataInfo<FabulousRecordVo> list(@Validated(QueryGroup.class) FabulousRecordBo bo, PageQuery pageQuery) {
        return iFabulousRecordService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出点赞列表
     */
    @SaCheckPermission("fabulous:record:export")
    @Log(title = "点赞", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated FabulousRecordBo bo, HttpServletResponse response) {
        List<FabulousRecordVo> list = iFabulousRecordService.queryList(bo);
        ExcelUtil.exportExcel(list, "点赞", FabulousRecordVo.class, response);
    }

    /**
     * 获取点赞详细信息
     */
    @SaCheckPermission("fabulous:record:query")
    @GetMapping("/{id}")
    public R<FabulousRecordVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iFabulousRecordService.queryById(id));
    }

    /**
     * 新增点赞
     */
    @SaCheckPermission("fabulous:record:add")
    @Log(title = "点赞", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody FabulousRecordBo bo) {
        return toAjax(iFabulousRecordService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改点赞
     */
    @SaCheckPermission("fabulous:record:edit")
    @Log(title = "点赞", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody FabulousRecordBo bo) {
        return toAjax(iFabulousRecordService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除点赞
     */
    @SaCheckPermission("fabulous:record:remove")
    @Log(title = "点赞", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iFabulousRecordService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
