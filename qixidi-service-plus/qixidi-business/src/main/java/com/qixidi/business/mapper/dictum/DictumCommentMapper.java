package com.qixidi.business.mapper.dictum;

import com.qixidi.business.domain.entity.dictum.DictumComment;
import com.qixidi.business.domain.vo.dictum.DictumCommentVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author zi-wei
 * @create 2024/12/17 14:13
 */
@Mapper
public interface DictumCommentMapper extends BaseMapperPlus<DictumCommentMapper, DictumComment, DictumCommentVo> {
}
