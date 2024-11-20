package com.aurora.business.service.collection;

import com.aurora.business.domain.bo.collection.CollectionRecordBo;
import com.aurora.business.domain.vo.collection.CollectionRecordVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 文章收藏关联Service接口
 *
 * @author aurora
 * @date 2022-11-10
 */
public interface ICollectionRecordService {

    /**
     * 查询文章收藏关联
     *
     * @param id 文章收藏关联主键
     * @return 文章收藏关联
     */
    CollectionRecordVo queryById(Long id);

    /**
     * 查询文章收藏关联列表
     *
     * @param bo 文章收藏关联
     * @return 文章收藏关联集合
     */
    TableDataInfo<CollectionRecordVo> queryPageList(CollectionRecordBo bo, PageQuery pageQuery);

    /**
     * 查询文章收藏关联列表
     *
     * @param bo 文章收藏关联
     * @return 文章收藏关联集合
     */
    List<CollectionRecordVo> queryList(CollectionRecordBo bo);

    /**
     * 修改文章收藏关联
     *
     * @param bo 文章收藏关联
     * @return 结果
     */
    Boolean insertByBo(CollectionRecordBo bo);

    /**
     * 修改文章收藏关联
     *
     * @param bo 文章收藏关联
     * @return 结果
     */
    Boolean updateByBo(CollectionRecordBo bo);

    /**
     * 校验并批量删除文章收藏关联信息
     *
     * @param ids 需要删除的文章收藏关联主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

