package com.aurora.web.controller.business.comment;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.comment.ArticleCommentBo;
import com.aurora.business.domain.vo.comment.ArticleCommentVo;
import com.aurora.business.service.comment.IArticleCommentService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import com.aurora.common.utils.poi.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 文章评论Controller
 *
 * @author aurora
 * @date 2022-11-03
 */
@Validated
@Api(value = "文章评论控制器", tags = {"文章评论管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/article/comment")
public class ArticleCommentController extends BaseController {

    private final IArticleCommentService iArticleCommentService;

    /**
     * 查询文章评论列表
     */
    @ApiOperation("查询文章评论列表")
    @SaCheckPermission("article:comment:list")
    @GetMapping("/list")
    public TableDataInfo<ArticleCommentVo> list(@Validated(QueryGroup.class) ArticleCommentBo bo, PageQuery pageQuery) {
        return iArticleCommentService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出文章评论列表
     */
    @ApiOperation("导出文章评论列表")
    @SaCheckPermission("article:comment:export")
    @Log(title = "文章评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated ArticleCommentBo bo, HttpServletResponse response) {
        List<ArticleCommentVo> list = iArticleCommentService.queryList(bo);
        ExcelUtil.exportExcel(list, "文章评论", ArticleCommentVo.class, response);
    }

    /**
     * 获取文章评论详细信息
     */
    @ApiOperation("获取文章评论详细信息")
    @SaCheckPermission("article:comment:query")
    @GetMapping("/{id}")
    public R<ArticleCommentVo> getInfo(@ApiParam("主键")
                                       @NotNull(message = "主键不能为空")
                                       @PathVariable("id") Long id) {
        return R.ok(iArticleCommentService.queryById(id));
    }

    /**
     * 新增文章评论
     */
    @ApiOperation("新增文章评论")
    @SaCheckPermission("article:comment:add")
    @Log(title = "文章评论", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody ArticleCommentBo bo) throws Exception {
        return toAjax(iArticleCommentService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改文章评论
     */
    @ApiOperation("修改文章评论")
    @SaCheckPermission("system:comment:edit")
    @Log(title = "文章评论", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody ArticleCommentBo bo) {
        return toAjax(iArticleCommentService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除文章评论
     */
    @ApiOperation("删除文章评论")
    @Log(title = "文章评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iArticleCommentService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

