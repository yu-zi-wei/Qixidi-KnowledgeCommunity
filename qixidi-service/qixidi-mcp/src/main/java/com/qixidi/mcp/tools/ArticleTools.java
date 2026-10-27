package com.qixidi.mcp.tools;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.enums.article.ArticleAuditStateEnums;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.service.article.IArticleInformationService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 文章 tool：复刻前台发布链路（insertByBo/updateByBo），提交后进入审核
 *
 * @author zi-wei
 */
@Component
public class ArticleTools {

    /**
     * @Lazy 断循环依赖：Spring AI 自动装配（deepSeekChatModel → toolCallingManager →
     * toolCallbackResolver → 本类）会把 ToolCallbackProvider 卷进 ChatModel 构造，
     * 而文章 Service 又依赖 aiGenerationService（ChatClient），不加 @Lazy 启动即
     * BeanCurrentlyInCreationException
     */
    private final IArticleInformationService iArticleInformationService;

    public ArticleTools(@Lazy IArticleInformationService iArticleInformationService) {
        this.iArticleInformationService = iArticleInformationService;
    }

    @Tool(name = "create_article", description = "创建或更新文章（更新时传 articleId），提交后进入审核(UNDER_REVIEW)。labelIds/groupingId/specialId 需先通过 get_publish_context 获取。")
    public String createArticle(
        @ToolParam(description = "文章标题") String title,
        @ToolParam(description = "Markdown 正文") String content,
        @ToolParam(description = "标签 id 列表（来自 get_publish_context.labels）", required = false) List<Long> labelIds,
        @ToolParam(description = "分类分组 id（来自 get_publish_context.groupings）") Long groupingId,
        @ToolParam(description = "专栏 id（来自 get_publish_context.specials，不投专栏可省略）", required = false) Long specialId,
        @ToolParam(description = "文章类型：1 原创 2 转载，默认 1", required = false) Integer type,
        @ToolParam(description = "转载原文链接（type=2 时必填）", required = false) String reprintUrl,
        @ToolParam(description = "自定义摘要（不填则由系统生成）", required = false) String articleAbstract,
        @ToolParam(description = "更新已有文章时传其 id", required = false) Long articleId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        if (groupingId == null) {
            throw new ServiceException("groupingId 不能为空，请先调用 get_publish_context 获取分类分组");
        }
        if (labelIds == null || labelIds.isEmpty()) {
            throw new ServiceException("labelIds 不能为空，请先调用 get_publish_context 获取标签");
        }
        ArticleInformationBo bo = new ArticleInformationBo();
        bo.setId(articleId);
        bo.setArticleTitle(title);
        bo.setArticleContent(content);
        bo.setType(type == null ? 1 : type);
        bo.setReprintUrl(reprintUrl);
        bo.setArticleAbstract(articleAbstract);
        bo.setGroupingId(groupingId);
        bo.setSpecialId(specialId);
        bo.setLabelId(labelIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        bo.setAuditState(ArticleAuditStateEnums.UNDER_REVIEW.getCode());
        Long id;
        if (bo.getId() != null) {
            ArticleInformationVo vo = iArticleInformationService.updateByBo(bo);
            id = vo.getId();
            if (id == null || id <= 0) {
                throw new ServiceException("文章更新失败");
            }
        } else {
            ArticleInformationVo vo = iArticleInformationService.insertByBo(bo);
            id = vo.getId();
            if (id == null || id <= 0) {
                throw new ServiceException("文章创建失败");
            }
        }
        Map<String, Object> result = new HashMap<>();
        result.put("articleId", id);
        result.put("auditState", "UNDER_REVIEW");
        return JSONUtil.toJsonStr(result);
    }

    @Tool(name = "list_my_articles", description = "分页查询当前用户的文章列表（含草稿），返回精简字段 id/title/auditState/updateTime。auditState 含义：1 审核中、2 审核通过、3 审核不通过、4 草稿。")
    public String listMyArticles(
        @ToolParam(description = "页码，从 1 开始，默认 1", required = false) Integer pageNum,
        @ToolParam(description = "每页条数，默认 10，最大 50", required = false) Integer pageSize
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        ArticleInformationBo bo = new ArticleInformationBo();
        // 强制按当前用户过滤（与 UserArticleController 列表同款），防止列出他人文章
        bo.setUserId(user.getUuid());
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNum == null || pageNum < 1 ? 1 : pageNum);
        pageQuery.setPageSize(pageSize == null || pageSize < 1 ? 10 : Math.min(pageSize, 50));
        Page<ArticleInformationVo> page = iArticleInformationService.getArticleInfo(bo, pageQuery);
        List<Map<String, Object>> rows = page.getRecords().stream().map(v -> {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("id", v.getId());
            row.put("title", v.getArticleTitle());
            row.put("auditState", v.getAuditState());
            row.put("updateTime", v.getUpdateTime());
            return row;
        }).collect(Collectors.toList());
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", page.getTotal());
        result.put("rows", rows);
        return JSONUtil.toJsonStr(result);
    }

    @Tool(name = "get_article", description = "获取指定文章的完整内容（Markdown 正文、标签、分类等），仅限本人的文章，用于编辑前取原文。")
    public ArticleInformationVo getArticle(
        @ToolParam(description = "文章 id（来自 list_my_articles 或 create_article 返回值）") Long articleId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // Service 层已做属主校验，非本人文章抛"无权操作他人内容"
        return iArticleInformationService.getArticle(articleId);
    }

    @Tool(name = "update_article", description = "更新已有文章，仅传需要修改的字段（未传字段保持不变），更新提交后重新进入审核。仅能更新自己的文章。")
    public String updateArticle(
        @ToolParam(description = "文章 id") Long articleId,
        @ToolParam(description = "新标题（不修改则不传）", required = false) String title,
        @ToolParam(description = "新 Markdown 正文（不修改则不传）", required = false) String content,
        @ToolParam(description = "标签 id 列表（不修改则不传）", required = false) List<Long> labelIds,
        @ToolParam(description = "分类分组 id（不修改则不传）", required = false) Long groupingId,
        @ToolParam(description = "专栏 id（不修改则不传）", required = false) Long specialId,
        @ToolParam(description = "文章类型：1 原创 2 转载（不修改则不传）", required = false) Integer type,
        @ToolParam(description = "转载原文链接（不修改则不传）", required = false) String reprintUrl,
        @ToolParam(description = "自定义摘要（不修改则不传）", required = false) String articleAbstract
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        ArticleInformationBo bo = new ArticleInformationBo();
        bo.setId(articleId);
        bo.setArticleTitle(title);
        bo.setArticleContent(content);
        bo.setGroupingId(groupingId);
        bo.setSpecialId(specialId);
        bo.setType(type);
        bo.setReprintUrl(reprintUrl);
        bo.setArticleAbstract(articleAbstract);
        if (labelIds != null && !labelIds.isEmpty()) {
            bo.setLabelId(labelIds.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        // 与前台编辑同语义：更新提交后重新进入审核
        bo.setAuditState(ArticleAuditStateEnums.UNDER_REVIEW.getCode());
        iArticleInformationService.updateByBo(bo);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("articleId", articleId);
        result.put("auditState", "UNDER_REVIEW");
        return JSONUtil.toJsonStr(result);
    }

    @Tool(name = "delete_article", description = "删除自己的文章，仅限本人。")
    public String deleteArticle(
        @ToolParam(description = "文章 id") Long articleId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // Service 层已做属主校验，非本人文章抛"无权操作他人内容"
        int rows = iArticleInformationService.delete(articleId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("articleId", articleId);
        result.put("deleted", rows > 0);
        return JSONUtil.toJsonStr(result);
    }
}
