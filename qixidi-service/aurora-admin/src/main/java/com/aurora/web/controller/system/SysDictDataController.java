package com.aurora.web.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import com.aurora.common.annotation.Log;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.domain.entity.SysDictData;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import com.aurora.system.service.ISysDictDataService;
import com.aurora.system.service.ISysDictTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息管理
 *
 * @author Lion Li
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    private final ISysDictDataService dictDataService;
    private final ISysDictTypeService dictTypeService;

    /**
     * 查询字典数据列表
     *
     * @param dictData
     * @param pageQuery
     * @return
     */
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    public TableDataInfo<SysDictData> list(SysDictData dictData, PageQuery pageQuery) {
        return dictDataService.selectPageDictDataList(dictData, pageQuery);
    }

    /**
     * 导出字典数据列表
     *
     * @param dictData
     * @param response
     */
    @Log(title = "字典数据", businessType = BusinessType.EXPORT)
    @SaCheckPermission("system:dict:export")
    @PostMapping("/export")
    public void export(SysDictData dictData, HttpServletResponse response) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil.exportExcel(list, "字典数据", SysDictData.class, response);
    }

    /**
     * 查询字典数据详细
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = "/{dictCode}")
    public R<SysDictData> getInfo(@PathVariable Long dictCode) {
        return R.ok(dictDataService.selectDictDataById(dictCode));
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{dictType}")
    public R<List<SysDictData>> dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return R.ok(data);
    }

    /**
     * 新增字典类型
     */
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Void> add(@Validated @RequestBody SysDictData dict) {
        return toAjax(dictDataService.insertDictData(dict));
    }

    /**
     * 修改保存字典类型
     */
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<Void> edit(@Validated @RequestBody SysDictData dict) {
        return toAjax(dictDataService.updateDictData(dict));
    }

    /**
     * 删除字典类型
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public R<Void> remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return R.ok();
    }
}
