package com.qixidi.business.api.frontDesk.collection;

import com.qixidi.business.domain.bo.collection.CollectionInformationBo;
import com.qixidi.business.domain.bo.collection.CollectionRecordBo;
import com.qixidi.business.domain.vo.collection.CollectionInformationVo;
import com.qixidi.business.service.collection.ICollectionInformationService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.helper.LoginHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotNull;

/**
 * 【前台】收藏夹信息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
public class CollectionFdkController extends BaseController {
    private final ICollectionInformationService iCollectionInformationService;


    /**
     * 查询收藏夹信息列表
     */
    @GetMapping("/white/collection/list/{uuid}")
    public R listUid(@NotNull(message = "用户id不能为空") @PathVariable("uuid") String uuid) {
        return R.ok(iCollectionInformationService.listUid(uuid));
    }

    /**
     * 查询收藏夹信息列表
     */
    @GetMapping("/frontDesk/collection/list")
    public R frontDeskListUid() {
        String tripartiteUuid = LoginHelper.getTripartiteUuid();
        if (tripartiteUuid == null) throw new RuntimeException("用户未登录");
        return R.ok(iCollectionInformationService.listUid(tripartiteUuid));
    }

    /**
     * 查询收藏夹信息列表
     *
     * @return
     */
    @GetMapping("/white/aut/collection/list")
    public R list() {
        return R.ok(iCollectionInformationService.list());
    }

    /**
     * 查询收藏夹文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/white/collection/article/list")
    public R articleList(CollectionRecordBo bo, PageQuery pageQuery) {
        return R.ok(iCollectionInformationService.articleList(bo, pageQuery));
    }

    /**
     * 获取收藏夹信息详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/white/collection/{id}")
    public R<CollectionInformationVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iCollectionInformationService.queryById(id));
    }

    /**
     * 新增收藏夹信息
     */
    @Log(title = "收藏夹信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/frontDesk/add/collection")
    public R<Void> addCollection(@Validated(AddGroup.class) @RequestBody CollectionInformationBo bo) {
        return toAjax(iCollectionInformationService.addCollection(bo) ? 1 : 0);
    }

    /**
     * 删除收藏夹信息
     */
    @Log(title = "收藏夹信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/frontDesk/delete/collection/{id}")
    public R<Void> removeCollection(@PathVariable Long id) {
        return toAjax(iCollectionInformationService.removeCollection(id));
    }

    /**
     * 添加收藏夹文章
     *
     * @param bo
     * @return
     */
    @Log(title = "添加收藏夹文章", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/frontDesk/add/collection/data")
    public R<Void> collectionArticle(@Validated(AddGroup.class) @RequestBody CollectionRecordBo bo) {
        return toAjax(iCollectionInformationService.collectionArticle(bo) ? 1 : 0);
    }

    /**
     * 转移收藏夹文章
     *
     * @param bo
     * @return
     */
    @Log(title = "转移收藏夹文章", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PutMapping("/frontDesk/update/collection/data")
    public R<Void> collectionUpdate(@RequestBody CollectionRecordBo bo) {
        return toAjax(iCollectionInformationService.collectionUpdate(bo) ? 1 : 0);
    }

    /**
     * 删除收藏夹文章
     *
     * @param id
     * @param labelId
     * @return
     */
    @Log(title = "删除收藏夹文章", businessType = BusinessType.INSERT)
    @GetMapping("/frontDesk/delete/collection/data/{id}/{labelId}")
    public R<Void> deleteCollectionArticle(@PathVariable("id") Long id, @PathVariable("labelId") String labelId) {
        return toAjax(iCollectionInformationService.deleteCollectionArticle(id, labelId) ? 1 : 0);
    }

}
