package com.qixidi.system.api.dictum;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.dictum.DictumAlbumBo;
import com.qixidi.business.domain.vo.dictum.DictumAlbumVo;
import com.qixidi.business.service.dictum.IDictumAlbumService;
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
 * 随笔专辑管理
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dictum/album")
@LogModule("随笔专辑")
public class DictumAlbumController {

    private final IDictumAlbumService iDictumAlbumService;

    /**
     * 查询随笔专辑列表
     */
    @SaCheckPermission("dictum:album:list")
    @PostMapping("/list")
    public TableDataInfo<DictumAlbumVo> list(@Validated(QueryGroup.class) @RequestBody DictumAlbumBo bo) {
        return iDictumAlbumService.queryPageList(bo, bo);
    }

    /**
     * 导出随笔专辑列表
     */
    @SaCheckPermission("dictum:album:export")
    @Log(title = "导出随笔专辑", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated DictumAlbumBo bo, HttpServletResponse response) {
        List<DictumAlbumVo> list = iDictumAlbumService.queryList(bo);
        ExcelUtil.exportExcel(list, "随笔专辑", DictumAlbumVo.class, response);
    }

    /**
     * 获取随笔专辑详细信息
     */
    @SaCheckPermission("dictum:album:query")
    @GetMapping("/{id}")
    public DictumAlbumVo getInfo(@PathVariable("id") Long id) {
        return iDictumAlbumService.queryById(id);
    }

    /**
     * 新增随笔专辑
     */
    @SaCheckPermission("dictum:album:add")
    @Log(title = "新增随笔专辑", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody DictumAlbumBo bo) {
        iDictumAlbumService.insertByBo(bo);
    }

    /**
     * 修改随笔专辑
     */
    @SaCheckPermission("dictum:album:edit")
    @Log(title = "修改随笔专辑", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody DictumAlbumBo bo) {
        iDictumAlbumService.updateByBo(bo);
    }

    /**
     * 删除随笔专辑
     */
    @SaCheckPermission("dictum:album:remove")
    @Log(title = "删除随笔专辑", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        iDictumAlbumService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}
