package com.aurora.web.controller.business.dictum;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.dictum.DictumInfoBo;
import com.aurora.business.domain.vo.dictum.DictumInfoVo;
import com.aurora.business.service.dictum.IDictumInfoService;
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
 * 名言信息Controller
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/dictum/info")
public class DictumInfoController extends BaseController {

    private final IDictumInfoService iDictumInfoService;

    /**
     * 查询名言信息列表
     */
    @SaCheckPermission("business:dictum:info:list")
    @GetMapping("/list")
    public TableDataInfo<DictumInfoVo> list(@Validated(QueryGroup.class) DictumInfoBo bo, PageQuery pageQuery) {
        return iDictumInfoService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出名言信息列表
     */
    @SaCheckPermission("business:dictum:info:export")
    @Log(title = "名言信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated DictumInfoBo bo, HttpServletResponse response) {
        List<DictumInfoVo> list = iDictumInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "名言信息", DictumInfoVo.class, response);
    }

    /**
     * 获取名言信息详细信息
     */
    @SaCheckPermission("business:dictum:info:query")
    @GetMapping("/{id}")
    public R<DictumInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iDictumInfoService.queryById(id));
    }

    /**
     * 新增名言信息
     */
    @SaCheckPermission("business:dictum:info:add")
    @Log(title = "名言信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DictumInfoBo bo) {
        return toAjax(iDictumInfoService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改名言信息
     */
    @SaCheckPermission("business:dictum:info:edit")
    @Log(title = "名言信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DictumInfoBo bo) {
        return toAjax(iDictumInfoService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除名言信息
     */
    @SaCheckPermission("business:dictum:info:remove")
    @Log(title = "名言信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iDictumInfoService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

