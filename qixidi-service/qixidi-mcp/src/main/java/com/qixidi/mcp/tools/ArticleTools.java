package com.qixidi.mcp.tools;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.enums.article.ArticleAuditStateEnums;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.service.article.IArticleInformationService;
import com.qixidi.mcp.domain.bo.article.McpArticleBo;
import com.qixidi.mcp.domain.vo.article.McpArticleBriefVo;
import com.qixidi.mcp.domain.vo.article.McpArticleDeleteVo;
import com.qixidi.mcp.domain.vo.article.McpArticleDetailVo;
import com.qixidi.mcp.domain.vo.article.McpArticlePageVo;
import com.qixidi.mcp.domain.vo.article.McpArticleSubmitVo;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;
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
    public McpArticleSubmitVo createArticle(
        @ToolParam(description = "文章内容对象") McpArticleBo bo
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        if (bo.getGroupingId() == null) {
            throw new ServiceException("groupingId 不能为空，请先调用 get_publish_context 获取分类分组");
        }
        if (bo.getLabelIds() == null || bo.getLabelIds().isEmpty()) {
            throw new ServiceException("labelIds 不能为空，请先调用 get_publish_context 获取标签");
        }
        ArticleInformationBo info = toInfoBo(bo);
        info.setType(bo.getType() == null ? 1 : bo.getType());
        info.setAuditState(ArticleAuditStateEnums.UNDER_REVIEW.getCode());
        Long id;
        if (info.getId() != null) {
            ArticleInformationVo vo = iArticleInformationService.updateByBo(info);
            id = vo.getId();
            if (id == null || id <= 0) {
                throw new ServiceException("文章更新失败");
            }
        } else {
            ArticleInformationVo vo = iArticleInformationService.insertByBo(info);
            id = vo.getId();
            if (id == null || id <= 0) {
                throw new ServiceException("文章创建失败");
            }
        }
        return submitVo(id);
    }

    @Tool(name = "list_my_articles", description = "分页查询当前用户的文章列表（含草稿），返回精简字段 id/title/auditState/updateTime。auditState 含义：1 审核中、2 审核通过、3 审核不通过、4 草稿。")
    public McpArticlePageVo listMyArticles(
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
        List<McpArticleBriefVo> rows = page.getRecords().stream().map(v -> {
            McpArticleBriefVo row = new McpArticleBriefVo();
            row.setId(v.getId());
            row.setTitle(v.getArticleTitle());
            row.setAuditState(v.getAuditState());
            row.setUpdateTime(DateUtil.formatDateTime(v.getUpdateTime()));
            return row;
        }).collect(Collectors.toList());
        McpArticlePageVo result = new McpArticlePageVo();
        result.setTotal(page.getTotal());
        result.setRows(rows);
        return result;
    }

    @Tool(name = "get_article", description = "获取指定文章的完整内容（Markdown 正文、标签、分类等），仅限本人的文章，用于编辑前取原文。")
    public McpArticleDetailVo getArticle(
        @ToolParam(description = "文章 id（来自 list_my_articles 或 create_article 返回值）") Long articleId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // Service 层已做属主校验，非本人文章抛"无权操作他人内容"
        ArticleInformationVo vo = iArticleInformationService.getArticle(articleId);
        McpArticleDetailVo detail = new McpArticleDetailVo();
        detail.setId(vo.getId());
        detail.setTitle(vo.getArticleTitle());
        detail.setContent(vo.getArticleContent());
        detail.setArticleAbstract(vo.getArticleAbstract());
        detail.setType(vo.getType());
        detail.setReprintUrl(vo.getReprintUrl());
        detail.setGroupingId(vo.getGroupingId());
        detail.setGroupingName(vo.getGroupingName());
        detail.setLabelIds(vo.getLabelLongList());
        detail.setLabelNames(vo.getLabelNameList());
        detail.setSpecialId(vo.getSpecialId());
        detail.setAuditState(vo.getAuditState());
        detail.setUpdateTime(DateUtil.formatDateTime(vo.getUpdateTime()));
        return detail;
    }

    @Tool(name = "update_article", description = "更新已有文章，仅传需要修改的字段（未传字段保持不变），更新提交后重新进入审核。仅能更新自己的文章。")
    public McpArticleSubmitVo updateArticle(
        @ToolParam(description = "文章内容对象，articleId 必传，其余仅传需修改字段") McpArticleBo bo
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        if (bo.getArticleId() == null) {
            throw new ServiceException("articleId 不能为空");
        }
        ArticleInformationBo info = toInfoBo(bo);
        // 与前台编辑同语义：更新提交后重新进入审核
        info.setAuditState(ArticleAuditStateEnums.UNDER_REVIEW.getCode());
        iArticleInformationService.updateByBo(info);
        return submitVo(bo.getArticleId());
    }

    @Tool(name = "delete_article", description = "删除自己的文章，仅限本人。")
    public McpArticleDeleteVo deleteArticle(
        @ToolParam(description = "文章 id") Long articleId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // Service 层已做属主校验，非本人文章抛"无权操作他人内容"
        int rows = iArticleInformationService.delete(articleId);
        McpArticleDeleteVo result = new McpArticleDeleteVo();
        result.setArticleId(articleId);
        result.setDeleted(rows > 0);
        return result;
    }

    /**
     * MCP 入参对象 → 业务 BO（字段映射集中一处，create/update 共用）
     */
    private ArticleInformationBo toInfoBo(McpArticleBo bo) {
        ArticleInformationBo info = new ArticleInformationBo();
        info.setId(bo.getArticleId());
        info.setArticleTitle(bo.getTitle());
        info.setArticleContent(bo.getContent());
        info.setGroupingId(bo.getGroupingId());
        info.setSpecialId(bo.getSpecialId());
        info.setType(bo.getType());
        info.setReprintUrl(bo.getReprintUrl());
        info.setArticleAbstract(bo.getArticleAbstract());
        info.setAbstractSelect(bo.getAbstractSelect());
        if (bo.getLabelIds() != null && !bo.getLabelIds().isEmpty()) {
            info.setLabelId(bo.getLabelIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        return info;
    }

    private McpArticleSubmitVo submitVo(Long articleId) {
        McpArticleSubmitVo vo = new McpArticleSubmitVo();
        vo.setArticleId(articleId);
        vo.setAuditState("UNDER_REVIEW");
        return vo;
    }
}
