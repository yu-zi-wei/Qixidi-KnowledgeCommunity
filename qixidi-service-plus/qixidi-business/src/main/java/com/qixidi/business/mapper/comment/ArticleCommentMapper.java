package com.qixidi.business.mapper.comment;

import com.qixidi.business.domain.entity.comment.ArticleComment;
import com.qixidi.business.domain.vo.comment.ArticleCommentVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 文章评论Mapper接口
 *
 * @author aurora
 * @date 2022-11-03
 */
@Mapper
public interface ArticleCommentMapper extends BaseMapperPlus<ArticleCommentMapper, ArticleComment, ArticleCommentVo> {

    List<ArticleCommentVo> selectBasicsLis(@Param("ids") Collection<Long> ids);
}

