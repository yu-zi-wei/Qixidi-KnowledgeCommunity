package com.aurora.web.controller.business.dictum;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.dictum.DictumAlbumBo;
import com.aurora.business.domain.vo.dictum.DictumAlbumVo;
import com.aurora.business.service.dictum.IDictumAlbumService;
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
 * 名言专辑管理
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/dictum/album")
public class DictumAlbumController extends BaseController {

    private final IDictumAlbumService iDictumAlbumService;

    /**
     * 查询名言专辑列表
     */
    @SaCheckPermission("business:dictum:album:list")
    @GetMapping("/list")
    public TableDataInfo<DictumAlbumVo> list(@Validated(QueryGroup.class) DictumAlbumBo bo, PageQuery pageQuery) {
        return iDictumAlbumService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出名言专辑列表
     */
    @SaCheckPermission("business:dictum:album:export")
    @Log(title = "名言专辑", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated DictumAlbumBo bo, HttpServletResponse response) {
        List<DictumAlbumVo> list = iDictumAlbumService.queryList(bo);
        ExcelUtil.exportExcel(list, "名言专辑", DictumAlbumVo.class, response);
    }

    /**
     * 获取名言专辑详细信息
     */
    @SaCheckPermission("business:dictum:album:query")
    @GetMapping("/{id}")
    public R<DictumAlbumVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iDictumAlbumService.queryById(id));
    }

    /**
     * 新增名言专辑
     */
    @SaCheckPermission("business:dictum:album:add")
    @Log(title = "名言专辑", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DictumAlbumBo bo) {
        return toAjax(iDictumAlbumService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改名言专辑
     */
    @SaCheckPermission("business:dictum:album:edit")
    @Log(title = "名言专辑", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DictumAlbumBo bo) {
        return toAjax(iDictumAlbumService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除名言专辑
     */
    @SaCheckPermission("business:dictum:album:remove")
    @Log(title = "名言专辑", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iDictumAlbumService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
