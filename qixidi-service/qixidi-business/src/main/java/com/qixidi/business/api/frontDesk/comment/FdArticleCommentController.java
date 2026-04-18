package com.qixidi.business.api.frontDesk.comment;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.annotation.Log;

import com.qixidi.business.domain.bo.comment.ArticleCommentBo;
import com.qixidi.business.domain.vo.comment.ArticleCommentVo;
import com.qixidi.business.service.comment.IArticleCommentService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 【前台】文章评论管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping()
public class FdArticleCommentController {

    private final IArticleCommentService iArticleCommentService;

    /**
     * 查询文章评论列表
     */
    @GetMapping("/white/article/comment/list")
    public List<ArticleCommentVo> list(@Validated(QueryGroup.class) ArticleCommentBo bo, PageQuery pageQuery) {
        return iArticleCommentService.ArticleList(bo, pageQuery);
    }

    /**
     * 新增文章评论
     */
    @Log(title = "文章评论", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/article/comment/insert")
    public void add(@Validated(AddGroup.class) @RequestBody ArticleCommentBo bo) throws Exception {
        iArticleCommentService.insertByBo(bo);
    }

    /**
     * 删除文章评论
     */
    @Log(title = "文章评论", businessType = BusinessType.DELETE)
    @PostMapping("/article/delete/comment")
    public void remove(@RequestBody ArticleCommentBo bo) {
        iArticleCommentService.deleteWithValidById(bo);
    }

    /**
     * 查看文章评论
     */
    @GetMapping("/get/comment/{id}")
    public Object getComment(@NotNull(message = "id不能为空") @PathVariable Long id) {
        return iArticleCommentService.getComment(id);
    }
}
