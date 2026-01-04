package com.qixidi.business.api.backstage.dictum;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.service.dictum.IDictumInfoService;
import com.qixidi.auth.annotation.Log;
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
 * 名言信息Controller
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/dictum/info")
public class DictumInfoController {

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
    public DictumInfoVo getInfo(@PathVariable("id") Long id) {
        return iDictumInfoService.queryById(id);
    }

    /**
     * 新增名言信息
     */
    @SaCheckPermission("business:dictum:info:add")
    @Log(title = "名言信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody DictumInfoBo bo) {
        iDictumInfoService.insertByBo(bo);
    }

    /**
     * 修改名言信息
     */
    @SaCheckPermission("business:dictum:info:edit")
    @Log(title = "名言信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody DictumInfoBo bo) {
        iDictumInfoService.updateByBo(bo);
    }

    /**
     * 删除名言信息
     */
    @SaCheckPermission("business:dictum:info:remove")
    @Log(title = "名言信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iDictumInfoService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

