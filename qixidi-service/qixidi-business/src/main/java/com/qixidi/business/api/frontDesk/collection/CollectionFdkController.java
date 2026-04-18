package com.qixidi.business.api.frontDesk.collection;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;

import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.collection.CollectionInformationBo;
import com.qixidi.business.domain.bo.collection.CollectionRecordBo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.collection.CollectionInformationVo;
import com.qixidi.business.service.collection.ICollectionInformationService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【前台】收藏夹信息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
public class CollectionFdkController {
    private final ICollectionInformationService iCollectionInformationService;


    /**
     * 查询收藏夹信息列表
     */
    @GetMapping("/white/collection/list/{uuid}")
    public List<CollectionInformationVo> listUid(@NotNull(message = "用户id不能为空") @PathVariable("uuid") String uuid) {
        return iCollectionInformationService.listUid(uuid);
    }

    /**
     * 查询收藏夹信息列表
     */
    @GetMapping("/frontDesk/collection/list")
    public List<CollectionInformationVo> frontDeskListUid() {
        String tripartiteUuid = LoginHelper.getTripartiteUuid();
        if (tripartiteUuid == null) throw new RuntimeException("用户未登录");
        return iCollectionInformationService.listUid(tripartiteUuid);
    }

    /**
     * 查询收藏夹信息列表
     *
     * @return
     */
    @GetMapping("/white/aut/collection/list")
    public List<CollectionInformationVo> list() {
        return iCollectionInformationService.list();
    }

    /**
     * 查询收藏夹文章列表
     *
     * @param bo
     * @param pageQuery
     * @return
     */
    @GetMapping("/white/collection/article/list")
    public TableDataInfo<ArticleInformationVo> articleList(CollectionRecordBo bo, PageQuery pageQuery) {
        return TableDataInfo.build(iCollectionInformationService.articleList(bo, pageQuery));
    }

    /**
     * 获取收藏夹信息详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/white/collection/{id}")
    public CollectionInformationVo getInfo(@PathVariable("id") Long id) {
        return iCollectionInformationService.queryById(id);
    }

    /**
     * 新增收藏夹信息
     */
    @Log(title = "收藏夹信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/frontDesk/add/collection")
    public void addCollection(@Validated(AddGroup.class) @RequestBody CollectionInformationBo bo) {
        iCollectionInformationService.addCollection(bo);
    }

    /**
     * 删除收藏夹信息
     */
    @Log(title = "收藏夹信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/frontDesk/delete/collection/{id}")
    public void removeCollection(@PathVariable Long id) {
        iCollectionInformationService.removeCollection(id);
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
    public void collectionArticle(@Validated(AddGroup.class) @RequestBody CollectionRecordBo bo) {
        iCollectionInformationService.collectionArticle(bo);
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
    public void collectionUpdate(@RequestBody CollectionRecordBo bo) {
        iCollectionInformationService.collectionUpdate(bo);
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
    public void deleteCollectionArticle(@PathVariable("id") Long id, @PathVariable("labelId") String labelId) {
        iCollectionInformationService.deleteCollectionArticle(id, labelId);
    }

}
