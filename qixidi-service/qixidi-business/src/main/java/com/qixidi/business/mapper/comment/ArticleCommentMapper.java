package com.qixidi.business.mapper.comment;

import com.qixidi.business.domain.entity.comment.ArticleComment;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.article.ArticleCountVo;
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

    /**
     * 后台列表 join 查询：评论人昵称/头像 + 所在文章标题（wrapper 列名须带 ac. 前缀）
     */
    IPage<ArticleCommentVo> selectCommentIndex(Page<ArticleComment> build, @Param(Constants.WRAPPER) QueryWrapper<ArticleComment> wrapper);

    List<ArticleCommentVo> selectBasicsLis(@Param("ids") Collection<Long> ids);

    List<CountUserWebsiteVo> selectCommentCountByUserIds(@Param("uuids") Collection<String> uuids);

    List<CountUserWebsiteVo> selectFansCommentCountByUserIds(@Param("uuids") Collection<String> uuids);

    List<ArticleCountVo> selectCommentCountByArticleIds(@Param("articleIds") Collection<Long> articleIds);
}

