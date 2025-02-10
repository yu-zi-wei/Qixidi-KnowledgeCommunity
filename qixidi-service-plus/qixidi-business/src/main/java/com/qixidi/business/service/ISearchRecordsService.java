package com.qixidi.business.service;

import com.qixidi.business.domain.bo.SearchRecordsBo;
import com.qixidi.business.domain.vo.SearchRecordsVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 搜索记录Service接口
 *
 * @author aurora
 * @date 2023-04-18
 */
public interface ISearchRecordsService {

    /**
     * 查询搜索记录
     *
     * @param id 搜索记录主键
     * @return 搜索记录
     */
    SearchRecordsVo queryById(Long id);

    /**
     * 查询搜索记录列表
     *
     * @param bo 搜索记录
     * @return 搜索记录集合
     */
    TableDataInfo<SearchRecordsVo> queryPageList(SearchRecordsBo bo, PageQuery pageQuery);

    /**
     * 查询搜索记录列表
     *
     * @param bo 搜索记录
     * @return 搜索记录集合
     */
    List<SearchRecordsVo> queryList(SearchRecordsBo bo);

    /**
     * 修改搜索记录
     *
     * @param bo 搜索记录
     * @return 结果
     */
    Boolean insertByBo(SearchRecordsBo bo);

    /**
     * 修改搜索记录
     *
     * @param bo 搜索记录
     * @return 结果
     */
    Boolean updateByBo(SearchRecordsBo bo);

    /**
     * 校验并批量删除搜索记录信息
     *
     * @param ids 需要删除的搜索记录主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<SearchRecordsVo> frontDeskList(SearchRecordsBo bo, PageQuery pageQuery);
}

