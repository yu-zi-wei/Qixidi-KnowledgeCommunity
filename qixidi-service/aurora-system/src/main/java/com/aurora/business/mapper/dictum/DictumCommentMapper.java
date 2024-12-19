package com.aurora.business.mapper.dictum;

import com.aurora.business.domain.entity.dictum.DictumComment;
import com.aurora.business.domain.vo.dictum.DictumCommentVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zi-wei
 * @create 2024/12/17 14:13
 */
@Mapper
public interface DictumCommentMapper extends BaseMapperPlus<DictumCommentMapper, DictumComment, DictumCommentVo> {
}
