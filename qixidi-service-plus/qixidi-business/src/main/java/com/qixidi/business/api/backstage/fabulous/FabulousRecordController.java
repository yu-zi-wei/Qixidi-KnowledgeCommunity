package com.qixidi.business.api.backstage.fabulous;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.fabulous.FabulousRecordBo;
import com.qixidi.business.domain.vo.fabulous.FabulousRecordVo;
import com.qixidi.business.service.fabulous.IFabulousRecordService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
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
