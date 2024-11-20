package com.aurora.web.controller.frontDesk.collection;

import com.aurora.business.domain.bo.collection.CollectionInformationBo;
import com.aurora.business.domain.bo.collection.CollectionRecordBo;
import com.aurora.business.domain.vo.collection.CollectionInformationVo;
import com.aurora.business.service.collection.ICollectionInformationService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Validated
@Api(value = "前台收藏夹信息控制器", tags = {"前台收藏夹信息管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/white")
public class CollectionFdkController extends BaseController {
    private final ICollectionInformationService iCollectionInformationService;

    /**
     * 新增收藏夹信息
     */
    @ApiOperation("新增收藏夹信息")
    @Log(title = "收藏夹信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/add/collection")
    public R<Void> addCollection(@Validated(AddGroup.class) @RequestBody CollectionInformationBo bo) {
        return toAjax(iCollectionInformationService.addCollection(bo) ? 1 : 0);
    }

    /**
     * 删除收藏夹信息
     */
    @ApiOperation("删除收藏夹信息")
    @Log(title = "收藏夹信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/delete/collection/{id}")
    public R<Void> removeCollection(@ApiParam("主键串")
                                    @NotNull(message = "主键不能为空") @PathVariable Long id) {
        return toAjax(iCollectionInformationService.removeCollection(id));
    }

    /**
     * 查询收藏夹信息列表
     */
    @ApiOperation("通过uuid查询收藏夹信息列表")
    @GetMapping("/collection/list/{uuid}")
    public R listUid(@NotNull(message = "用户id不能为空") @PathVariable("uuid") String uuid) {
        return R.ok(iCollectionInformationService.listUid(uuid));
    }

    @ApiOperation("查询收藏夹信息列表")
    @GetMapping("/aut/collection/list")
    public R list() {
        return R.ok(iCollectionInformationService.list());
    }

    @ApiOperation("查询收藏夹文章列表")
    @GetMapping("/collection/article/list")
    public R articleList(CollectionRecordBo bo, PageQuery pageQuery) {
        return R.ok(iCollectionInformationService.articleList(bo, pageQuery));
    }

    @ApiOperation("获取收藏夹信息详细信息")
    @GetMapping("/collection/{id}")
    public R<CollectionInformationVo> getInfo(@ApiParam("主键")
                                              @NotNull(message = "id不能为空")
                                              @PathVariable("id") Long id) {
        return R.ok(iCollectionInformationService.queryById(id));
    }

    @ApiOperation("添加收藏夹文章")
    @Log(title = "添加收藏夹文章", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/add/collection/data")
    public R<Void> collectionArticle(@Validated(AddGroup.class) @RequestBody CollectionRecordBo bo) {
        return toAjax(iCollectionInformationService.collectionArticle(bo) ? 1 : 0);
    }

    @ApiOperation("转移收藏夹文章")
    @Log(title = "转移收藏夹文章", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PutMapping("/update/collection/data")
    public R<Void> collectionUpdate(@RequestBody CollectionRecordBo bo) {
        return toAjax(iCollectionInformationService.collectionUpdate(bo) ? 1 : 0);
    }

    @ApiOperation("删除收藏夹文章")
    @Log(title = "删除收藏夹文章", businessType = BusinessType.INSERT)
    @GetMapping("/delete/collection/data/{id}/{labelId}")
    public R<Void> deleteCollectionArticle(@ApiParam("主键") @NotNull(message = "id不能为空")
                                           @PathVariable("id") Long id,@PathVariable("labelId") String  labelId) {
        return toAjax(iCollectionInformationService.deleteCollectionArticle(id,labelId) ? 1 : 0);
    }

}
