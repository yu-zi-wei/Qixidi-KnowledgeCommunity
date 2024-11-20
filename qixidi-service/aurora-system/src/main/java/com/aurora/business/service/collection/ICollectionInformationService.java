package com.aurora.business.service.collection;

import com.aurora.business.domain.bo.collection.CollectionInformationBo;
import com.aurora.business.domain.bo.collection.CollectionRecordBo;
import com.aurora.business.domain.vo.article.ArticleInformationVo;
import com.aurora.business.domain.vo.collection.CollectionInformationVo;
import com.aurora.common.core.domain.CensusEntity;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.vo.CensusVo;
import com.aurora.common.core.page.TableDataInfo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collection;
import java.util.List;

/**
 * 收藏夹信息Service接口
 *
 * @author aurora
 * @date 2022-09-29
 */
public interface ICollectionInformationService {

    /**
     * 查询收藏夹信息
     *
     * @param id 收藏夹信息主键
     * @return 收藏夹信息
     */
    CollectionInformationVo queryById(Long id);

    /**
     * 查询收藏夹信息列表
     *
     * @param bo 收藏夹信息
     * @return 收藏夹信息集合
     */
    TableDataInfo<CollectionInformationVo> queryPageList(CollectionInformationBo bo, PageQuery pageQuery);

    /**
     * 查询收藏夹信息列表
     *
     * @param bo 收藏夹信息
     * @return 收藏夹信息集合
     */
    List<CollectionInformationVo> queryList(CollectionInformationBo bo);

    /**
     * 修改收藏夹信息
     *
     * @param bo 收藏夹信息
     * @return 结果
     */
    Boolean insertByBo(CollectionInformationBo bo);

    /**
     * 修改收藏夹信息
     *
     * @param bo 收藏夹信息
     * @return 结果
     */
    Boolean updateByBo(CollectionInformationBo bo);

    /**
     * 校验并批量删除收藏夹信息信息
     *
     * @param ids     需要删除的收藏夹信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<CollectionInformationVo> listUid(String uuid);

    List<CollectionInformationVo> list();

    Page<ArticleInformationVo> articleList(CollectionRecordBo bo, PageQuery pageQuery);

    boolean collectionArticle(CollectionRecordBo bo);

    boolean deleteCollectionArticle(Long id, String labelId);

    boolean collectionUpdate(CollectionRecordBo bo);

    boolean addCollection(CollectionInformationBo bo);

    boolean removeCollection(Long id);

    List<CensusVo> timeCollectionCensus(CensusEntity bo);
}

