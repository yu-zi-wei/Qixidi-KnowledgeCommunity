package com.qixidi.business.mapper;

import com.qixidi.business.domain.entity.Feedback;
import com.qixidi.business.domain.vo.FeedbackVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
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

