package com.qixidi.business.api.dictum;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;

import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.dictum.DictumAlbumBo;
import com.qixidi.business.domain.vo.dictum.DictumAlbumVo;
import com.qixidi.business.service.dictum.IDictumAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 【前台】随笔专辑管理
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/dictum/album")
@LogModule("随笔专辑")
public class FdDictumAlbumController {

    private final IDictumAlbumService iDictumAlbumService;

    /**
     * 查询随笔专辑列表(后台)
     */
    @PostMapping("/role/list")
    public TableDataInfo<DictumAlbumVo> roleList(@Validated(QueryGroup.class) @RequestBody DictumAlbumBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        return iDictumAlbumService.queryPageList(bo, bo);
    }


    /**
     * 获取随笔专辑详细信息
     */
    @GetMapping("/{id}")
    public DictumAlbumVo getInfo(@PathVariable("id") Long id) {
        return iDictumAlbumService.queryById(id);
    }

    /**
     * 新增随笔专辑
     */
    @Log(title = "新增随笔专辑", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public Long add(@Validated(AddGroup.class) @RequestBody DictumAlbumBo bo) {
        if (!iDictumAlbumService.insertByBo(bo)) throw new ServiceException("创建随笔失败");
        return bo.getId();
    }

    /**
     * 修改随笔专辑
     */
    @Log(title = "修改随笔专辑", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody DictumAlbumBo bo) {
        if (!iDictumAlbumService.updateByBo(bo)) throw new ServiceException("更新随笔失败");
    }

    /**
     * 删除随笔专辑
     */
    @Log(title = "删除随笔专辑", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) throws Exception {
        if (!iDictumAlbumService.deleteWithValidById(id)) throw new ServiceException("删除随笔失败");
    }
}
