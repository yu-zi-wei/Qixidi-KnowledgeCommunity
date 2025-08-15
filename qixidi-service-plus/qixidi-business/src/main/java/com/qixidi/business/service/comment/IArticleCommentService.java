package com.qixidi.business.service.comment;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.comment.ArticleCommentBo;
import com.qixidi.business.domain.vo.comment.ArticleCommentVo;

import java.util.Collection;
import java.util.List;

/**
 * 文章评论Service接口
 *
 * @author aurora
 * @date 2022-11-03
 */
public interface IArticleCommentService {

    /**
     * 查询文章评论
     *
     * @param id 文章评论主键
     * @return 文章评论
     */
    ArticleCommentVo queryById(Long id);

    /**
     * 查询文章评论列表
     *
     * @param bo 文章评论
     * @return 文章评论集合
     */
    TableDataInfo<ArticleCommentVo> queryPageList(ArticleCommentBo bo, PageQuery pageQuery);

    /**
     * 查询文章评论列表
     *
     * @param bo 文章评论
     * @return 文章评论集合
     */
    List<ArticleCommentVo> queryList(ArticleCommentBo bo);

    /**
     * 修改文章评论
     *
     * @param bo 文章评论
     * @return 结果
     */
    Boolean insertByBo(ArticleCommentBo bo) throws Exception;

    /**
     * 修改文章评论
     *
     * @param bo 文章评论
     * @return 结果
     */
    Boolean updateByBo(ArticleCommentBo bo);

    /**
     * 校验并批量删除文章评论信息
     *
     * @param ids     需要删除的文章评论主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<ArticleCommentVo> ArticleList(ArticleCommentBo bo, PageQuery pageQuery);

    boolean deleteWithValidById(ArticleCommentBo bo);

    R getComment(Long id);

    Long fillArticleGetCount(Long id);
}
