package com.qixidi.business.api.backstage.comment;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.comment.ArticleCommentBo;
import com.qixidi.business.domain.vo.comment.ArticleCommentVo;
import com.qixidi.business.service.comment.IArticleCommentService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * 文章评论管理
 *
 * @author aurora
 * @date 2022-11-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/article/comment")
public class ArticleCommentController extends BaseController {

    private final IArticleCommentService iArticleCommentService;

    /**
     * 查询文章评论列表
     */
    @SaCheckPermission("article:comment:list")
    @GetMapping("/list")
    public TableDataInfo<ArticleCommentVo> list(@Validated(QueryGroup.class) ArticleCommentBo bo, PageQuery pageQuery) {
        return iArticleCommentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出文章评论列表
     */
    @SaCheckPermission("article:comment:export")
    @Log(title = "文章评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ArticleCommentBo bo, HttpServletResponse response) {
        List<ArticleCommentVo> list = iArticleCommentService.queryList(bo);
        ExcelUtil.exportExcel(list, "文章评论", ArticleCommentVo.class, response);
    }

    /**
     * 获取文章评论详细信息
     *
     * @param id
     * @return
     */
    @SaCheckPermission("article:comment:query")
    @GetMapping("/{id}")
    public R<ArticleCommentVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(iArticleCommentService.queryById(id));
    }

    /**
     * 新增文章评论
     *
     * @param bo
     * @return
     * @throws Exception
     */
    @SaCheckPermission("article:comment:add")
    @Log(title = "文章评论", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ArticleCommentBo bo) throws Exception {
        return toAjax(iArticleCommentService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改文章评论
     *
     * @param bo
     * @return
     */
    @SaCheckPermission("system:comment:edit")
    @Log(title = "文章评论", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ArticleCommentBo bo) {
        return toAjax(iArticleCommentService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除文章评论
     *
     * @param ids
     * @return
     */
    @Log(title = "文章评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(iArticleCommentService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

