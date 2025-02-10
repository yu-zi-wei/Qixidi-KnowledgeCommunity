package com.qixidi.business.service;

import com.qixidi.business.domain.bo.FeedbackBo;
import com.qixidi.business.domain.vo.FeedbackStatusSumVo;
import com.qixidi.business.domain.vo.FeedbackVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户反馈Service接口
 *
 * @author aurora
 * @date 2023-04-17
 */
public interface IFeedbackService {

    /**
     * 查询用户反馈
     *
     * @param id 用户反馈主键
     * @return 用户反馈
     */
    FeedbackVo queryById(Long id);

    /**
     * 查询用户反馈列表
     *
     * @param bo 用户反馈
     * @return 用户反馈集合
     */
    TableDataInfo<FeedbackVo> queryPageList(FeedbackBo bo, PageQuery pageQuery);

    /**
     * 查询用户反馈列表
     *
     * @param bo 用户反馈
     * @return 用户反馈集合
     */
    List<FeedbackVo> queryList(FeedbackBo bo);

    /**
     * 修改用户反馈
     *
     * @param bo 用户反馈
     * @return 结果
     */
    Boolean insertByBo(FeedbackBo bo);

    /**
     * 修改用户反馈
     *
     * @param bo 用户反馈
     * @return 结果
     */
    Boolean updateByBo(FeedbackBo bo);

    /**
     * 校验并批量删除用户反馈信息
     *
     * @param ids 需要删除的用户反馈主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean updateStatus(Long id, Integer status);

    FeedbackStatusSumVo statusSum();
}

