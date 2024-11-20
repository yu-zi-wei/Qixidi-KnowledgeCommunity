package com.aurora.business.service.topic;

import com.aurora.business.domain.bo.topic.TopicJoinInfoBo;
import com.aurora.business.domain.vo.topic.TopicJoinInfoVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 话题参与信息Service接口
 *
 * @author aurora
 * @date 2023-03-09
 */
public interface ITopicJoinInfoService {

    /**
     * 查询话题参与信息
     *
     * @param id 话题参与信息主键
     * @return 话题参与信息
     */
    TopicJoinInfoVo queryById(Long id);

    /**
     * 查询话题参与信息列表
     *
     * @param bo 话题参与信息
     * @return 话题参与信息集合
     */
    TableDataInfo<TopicJoinInfoVo> queryPageList(TopicJoinInfoBo bo, PageQuery pageQuery);

    /**
     * 查询话题参与信息列表
     *
     * @param bo 话题参与信息
     * @return 话题参与信息集合
     */
    List<TopicJoinInfoVo> queryList(TopicJoinInfoBo bo);

    /**
     * 修改话题参与信息
     *
     * @param bo 话题参与信息
     * @return 结果
     */
    Boolean insertByBo(TopicJoinInfoBo bo);

    /**
     * 修改话题参与信息
     *
     * @param bo 话题参与信息
     * @return 结果
     */
    Boolean updateByBo(TopicJoinInfoBo bo);

    /**
     * 校验并批量删除话题参与信息信息
     *
     * @param ids 需要删除的话题参与信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
