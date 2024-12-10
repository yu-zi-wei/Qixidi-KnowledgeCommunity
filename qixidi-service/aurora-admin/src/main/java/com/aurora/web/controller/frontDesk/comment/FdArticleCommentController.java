package com.aurora.web.controller.frontDesk.comment;


import com.aurora.business.domain.bo.comment.ArticleCommentBo;
import com.aurora.business.service.comment.IArticleCommentService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 【前台】文章评论管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping()
public class FdArticleCommentController extends BaseController {

    private final IArticleCommentService iArticleCommentService;

    /**
     * 查询文章评论列表
     */
    @GetMapping("/white/article/comment/list")
    public R list(@Validated(QueryGroup.class) ArticleCommentBo bo, PageQuery pageQuery) {
        return R.ok(iArticleCommentService.ArticleList(bo, pageQuery));
    }

    /**
     * 新增文章评论
     */
    @Log(title = "文章评论", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/article/comment/insert")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ArticleCommentBo bo) throws Exception {
        return toAjax(iArticleCommentService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 删除文章评论
     */
    @Log(title = "文章评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/article/comment")
    public R<Void> remove(ArticleCommentBo bo) {
        return toAjax(iArticleCommentService.deleteWithValidById(bo) ? 1 : 0);
    }

    /**
     * 查看文章评论
     */
    @GetMapping("/get/comment/{id}")
    public R<Void> getComment(@NotNull(message = "id不能为空") @PathVariable Long id) {
        return iArticleCommentService.getComment(id);
    }
}
