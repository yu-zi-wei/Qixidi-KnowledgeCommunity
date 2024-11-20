package com.aurora.business.mapper;

import com.aurora.business.domain.entity.Feedback;
import com.aurora.business.domain.vo.FeedbackVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户反馈Mapper接口
 *
 * @author aurora
 * @date 2023-04-17
 */
@Mapper
public interface FeedbackMapper extends BaseMapperPlus<FeedbackMapper, Feedback, FeedbackVo> {

}

