package com.aurora.web.controller.business.collection;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.collection.CollectionInformationBo;
import com.aurora.business.domain.vo.collection.CollectionInformationVo;
import com.aurora.business.service.collection.ICollectionInformationService;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 收藏夹信息Controller
 *
 * @author aurora
 * @date 2022-09-29
 */
@Validated
@Api(value = "收藏夹信息控制器", tags = {"收藏夹信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/collection/information")
public class CollectionInformationController extends BaseController {

    private final ICollectionInformationService iCollectionInformationService;

    /**
     * 查询收藏夹信息列表
     */
    @ApiOperation("查询收藏夹信息列表")
    @SaCheckPermission("collection:information:list")
    @GetMapping("/list")
    public TableDataInfo<CollectionInformationVo> list(@Validated(QueryGroup.class) CollectionInformationBo bo, PageQuery pageQuery) {
        return iCollectionInformationService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出收藏夹信息列表
     */
    @ApiOperation("导出收藏夹信息列表")
    @SaCheckPermission("collection:information:export")
    @Log(title = "收藏夹信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated CollectionInformationBo bo, HttpServletResponse response) {
        List<CollectionInformationVo> list = iCollectionInformationService.queryList(bo);
        ExcelUtil.exportExcel(list, "收藏夹信息", CollectionInformationVo.class, response);
    }

    /**
     * 获取收藏夹信息详细信息
     */
    @ApiOperation("获取收藏夹信息详细信息")
    @SaCheckPermission("collection:information:query")
    @GetMapping("/{id}")
    public R<CollectionInformationVo> getInfo(@ApiParam("主键")
                                              @NotNull(message = "主键不能为空")
                                              @PathVariable("id") Long id) {
        return R.ok(iCollectionInformationService.queryById(id));
    }

    /**
     * 新增收藏夹信息
     */
    @ApiOperation("新增收藏夹信息")
    @Log(title = "收藏夹信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CollectionInformationBo bo) {
        return toAjax(iCollectionInformationService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改收藏夹信息
     */
    @ApiOperation("修改收藏夹信息")
    @Log(title = "收藏夹信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CollectionInformationBo bo) {
        return toAjax(iCollectionInformationService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除收藏夹信息
     */
    @ApiOperation("删除收藏夹信息")
    @Log(title = "收藏夹信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iCollectionInformationService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

