package com.qixidi.business.comtroller.frontDesk.dictum;

import com.qixidi.business.domain.bo.dictum.DictumAlbumBo;
import com.qixidi.business.domain.vo.dictum.DictumAlbumVo;
import com.qixidi.business.service.dictum.IDictumAlbumService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.controller.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.helper.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【前台】名言专辑管理
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/dictum/album")
public class FdDictumAlbumController extends BaseController {

    private final IDictumAlbumService iDictumAlbumService;

    /**
     * 查询名言专辑列表(后台)
     */
    @GetMapping("/role/list")
    public TableDataInfo<DictumAlbumVo> roleList(@Validated(QueryGroup.class) DictumAlbumBo bo, PageQuery pageQuery) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        return iDictumAlbumService.queryPageList(bo, pageQuery);
    }


    /**
     * 获取名言专辑详细信息
     */
    @GetMapping("/{id}")
    public R<DictumAlbumVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iDictumAlbumService.queryById(id));
    }

    /**
     * 新增名言专辑
     */
    @Log(title = "名言专辑", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DictumAlbumBo bo) {
        return toAjax(iDictumAlbumService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改名言专辑
     */
    @Log(title = "名言专辑", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DictumAlbumBo bo) {
        return toAjax(iDictumAlbumService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除名言专辑
     */
    @Log(title = "名言专辑", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@PathVariable Long id) throws Exception {
        return toAjax(iDictumAlbumService.deleteWithValidById(id) ? 1 : 0);
    }
}
