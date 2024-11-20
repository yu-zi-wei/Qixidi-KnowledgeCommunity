package com.aurora.business.service.topic;

import com.aurora.business.domain.bo.topic.TopicInfoBo;
import com.aurora.business.domain.vo.topic.TopicInfoVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 话题信息Service接口
 *
 * @author aurora
 * @date 2023-03-09
 */
public interface ITopicInfoService {

    /**
     * 查询话题信息
     *
     * @param id 话题信息主键
     * @return 话题信息
     */
    TopicInfoVo queryById(Long id);

    /**
     * 查询话题信息列表
     *
     * @param bo 话题信息
     * @return 话题信息集合
     */
    TableDataInfo<TopicInfoVo> queryPageList(TopicInfoBo bo, PageQuery pageQuery);

    /**
     * 查询话题信息列表
     *
     * @param bo 话题信息
     * @return 话题信息集合
     */
    List<TopicInfoVo> queryList(TopicInfoBo bo);

    /**
     * 修改话题信息
     *
     * @param bo 话题信息
     * @return 结果
     */
    Boolean insertByBo(TopicInfoBo bo);

    /**
     * 修改话题信息
     *
     * @param bo 话题信息
     * @return 结果
     */
    Boolean updateByBo(TopicInfoBo bo);

    /**
     * 校验并批量删除话题信息信息
     *
     * @param ids 需要删除的话题信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
