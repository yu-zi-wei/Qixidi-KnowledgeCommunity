package com.qixidi.business.service.news;

import com.qixidi.business.domain.bo.news.NewsSystemInfoBo;
import com.qixidi.business.domain.vo.news.NewsSystemInfoVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

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
     * 失效/恢复系统消息（终审操作：只切 state）
     *
     * @param id    系统消息主键
     * @param state 目标状态（0：生效，1：失效）
     * @return 结果
     */
    Boolean updateState(Long id, Long state);

    /**
     * 校验并批量删除系统消息信息
     *
     * @param ids 需要删除的系统消息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

