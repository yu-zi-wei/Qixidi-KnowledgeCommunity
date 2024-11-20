package com.aurora.web.controller.frontDesk.dictum;

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
import com.aurora.common.helper.LoginHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 名言专辑Controller
 *
 * @author aurora
 * @date 2023-04-24
 */
@Validated
@Api(value = "名言专辑控制器", tags = {"名言专辑管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/dictum/album")
public class FdDictumAlbumController extends BaseController {

    private final IDictumAlbumService iDictumAlbumService;

    /**
     * 查询名言专辑列表(后台)
     */
    @ApiOperation("查询名言专辑列表(后台)")
    @GetMapping("/role/list")

    public TableDataInfo<DictumAlbumVo> roleList(@Validated(QueryGroup.class) DictumAlbumBo bo, PageQuery pageQuery) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        return iDictumAlbumService.queryPageList(bo, pageQuery);
    }


    /**
     * 获取名言专辑详细信息
     */
    @ApiOperation("获取名言专辑详细信息")
    @GetMapping("/{id}")
    public R<DictumAlbumVo> getInfo(@ApiParam("主键")
                                    @NotNull(message = "主键不能为空")
                                    @PathVariable("id") Long id) {
        return R.ok(iDictumAlbumService.queryById(id));
    }

    /**
     * 新增名言专辑
     */
    @ApiOperation("新增名言专辑")
    @Log(title = "名言专辑", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody DictumAlbumBo bo) {
        return toAjax(iDictumAlbumService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改名言专辑
     */
    @ApiOperation("修改名言专辑")
    @Log(title = "名言专辑", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody DictumAlbumBo bo) {
        return toAjax(iDictumAlbumService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除名言专辑
     */
    @ApiOperation("删除名言专辑")
    @Log(title = "名言专辑", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotNull(message = "主键不能为空")
                          @PathVariable Long id) throws Exception {
        return toAjax(iDictumAlbumService.deleteWithValidById(id) ? 1 : 0);
    }
}
