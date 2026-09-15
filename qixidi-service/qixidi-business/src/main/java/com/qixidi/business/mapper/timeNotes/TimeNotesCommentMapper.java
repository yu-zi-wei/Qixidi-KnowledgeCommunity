package com.qixidi.business.mapper.timeNotes;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.timeNotes.TimeNotesComment;
import com.qixidi.business.domain.vo.timeNotes.TimeNotesCommentVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 时光小记评论 Mapper
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@Mapper
public interface TimeNotesCommentMapper extends BaseMapperPlus<TimeNotesCommentMapper, TimeNotesComment, TimeNotesCommentVo> {
}
