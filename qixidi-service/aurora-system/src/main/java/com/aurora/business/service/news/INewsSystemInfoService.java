package com.aurora.business.service.news;

import com.aurora.business.domain.bo.news.NewsSystemInfoBo;
import com.aurora.business.domain.vo.news.NewsSystemInfoVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 系统消息Service接口
 *
 * @author aurora
 * @date 2023-04-23
 */
public interface INewsSystemInfoService {

    /**
     * 查询系统消息
     *
     * @param id 系统消息主键
     * @return 系统消息
     */
    NewsSystemInfoVo queryById(Long id);

    /**
     * 查询系统消息列表
     *
     * @param bo 系统消息
     * @return 系统消息集合
     */
    TableDataInfo<NewsSystemInfoVo> queryPageList(NewsSystemInfoBo bo, PageQuery pageQuery);

    /**
     * 查询系统消息列表
     *
     * @param bo 系统消息
     * @return 系统消息集合
     */
    List<NewsSystemInfoVo> queryList(NewsSystemInfoBo bo);

    /**
     * 修改系统消息
     *
     * @param bo 系统消息
     * @return 结果
     */
    Boolean insertByBo(NewsSystemInfoBo bo);

    /**
     * 修改系统消息
     *
     * @param bo 系统消息
     * @return 结果
     */
    Boolean updateByBo(NewsSystemInfoBo bo);

    /**
     * 校验并批量删除系统消息信息
     *
     * @param ids 需要删除的系统消息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

