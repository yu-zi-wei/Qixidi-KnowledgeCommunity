package com.qixidi.mcp.tools;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.enums.BusinessType;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.auth.helper.OperLogRecorder;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.ArticleInformationTwoBo;
import com.qixidi.business.domain.enums.article.ArticleAuditStateEnums;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.service.article.IArticleInformationService;
import com.qixidi.mcp.domain.bo.article.McpArticleBo;
import com.qixidi.mcp.domain.bo.article.McpArticleQueryBo;
import com.qixidi.mcp.domain.vo.article.McpArticleDeleteVo;
import com.qixidi.mcp.domain.vo.article.McpArticleDetailVo;
import com.qixidi.mcp.domain.vo.article.McpArticlePageVo;
import com.qixidi.mcp.domain.vo.article.McpArticleSearchItemVo;
import com.qixidi.mcp.domain.vo.article.McpArticleSubmitVo;
import com.qixidi.mcp.utils.McpPageUtils;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    private final OperLogRecorder operLogRecorder;

    public ArticleTools(@Lazy IArticleInformationService iArticleInformationService, OperLogRecorder operLogRecorder) {
        this.iArticleInformationService = iArticleInformationService;
        this.operLogRecorder = operLogRecorder;
    }

    @Tool(name = "create_article", description = """
        创建或更新文章并提交审核（auditState=UNDER_REVIEW）。

        必传：title / content / labelIds / groupingId，其中 labelIds / groupingId 先调 get_publish_context 获取。""")
    public McpArticleSubmitVo createArticle(
        @ToolParam(description = "文章内容对象") McpArticleBo bo
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        checkTitleAndContent(bo);
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
        operLogRecorder.record("MCP文章", "创建文章", BusinessType.INSERT,
            user.getUsername(), "id=" + id + ", title=" + bo.getTitle(), null);
        return submitVo(id, "UNDER_REVIEW");
    }

    @Tool(name = "search_articles", description = """
        多条件搜索文章：关键词（匹配标题+摘要）、摘要、审核状态多选、类型、更新时间范围、作者昵称、专栏。

        数据范围：
        - 不传 authorName：查本人全部状态（查自己的文章列表即用本工具，不传条件即可）
        - 传 authorName：查该作者的已发布文章

        返回 {total, rows[]}，rows 为元数据（不含正文，选中后用 get_article 查详情，仅限本人文章）：
        id / title / articleAbstract / type(1 原创 2 转载) / auditState(1 审核中 2 已发布 3 审核不通过 4 草稿) / authorName / updateTime(yyyy-MM-dd HH:mm:ss)""")
    public McpArticlePageVo searchArticles(
        @ToolParam(description = "查询条件对象") McpArticleQueryBo query,
        @ToolParam(description = "页码，从 1 开始，默认 1", required = false) Integer pageNum,
        @ToolParam(description = "每页条数，默认 100，最大 500", required = false) Integer pageSize
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        ArticleInformationBo bo = new ArticleInformationBo();
        bo.setArticleTitle(query.getTitle());
        bo.setArticleAbstract(query.getArticleAbstract());
        bo.setType(query.getType());
        bo.setSpecialId(query.getSpecialId());
        if (query.getAuthorName() == null || query.getAuthorName().isBlank()) {
            // 查本人：状态由调用方指定（缺省全部）
            bo.setUserId(user.getUuid());
            bo.setAuditStates(query.getAuditStates());
        } else {
            // 跨作者：只放开已发布文章，忽略调用方传入的状态
            bo.setNickname(query.getAuthorName().trim());
            bo.setAuditStates(List.of(ArticleAuditStateEnums.APPROV.getCode()));
        }
        LocalDate begin = parseDate(query.getBeginDate());
        LocalDate end = parseDate(query.getEndDate());
        if (begin != null) {
            bo.getParams().put("beginTime", begin + " 00:00:00");
        }
        if (end != null) {
            bo.getParams().put("endTime", end + " 23:59:59");
        }
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(McpPageUtils.pageNum(pageNum));
        pageQuery.setPageSize(McpPageUtils.pageSize(pageSize));
        Page<ArticleInformationVo> page = iArticleInformationService.getArticleInfo(bo, pageQuery);
        List<McpArticleSearchItemVo> rows = page.getRecords().stream().map(v -> {
            McpArticleSearchItemVo row = new McpArticleSearchItemVo();
            row.setId(v.getId());
            row.setTitle(v.getArticleTitle());
            row.setArticleAbstract(v.getArticleAbstract());
            row.setType(v.getType());
            row.setAuditState(v.getAuditState());
            row.setAuthorName(v.getNickname());
            row.setUpdateTime(DateUtil.formatDateTime(v.getUpdateTime()));
            return row;
        }).collect(Collectors.toList());
        McpArticlePageVo result = new McpArticlePageVo();
        result.setTotal(page.getTotal());
        result.setRows(rows);
        return result;
    }

    @Tool(name = "get_article", description = """
        获取指定文章的完整内容，仅限本人文章，用于编辑前取原文或查看详情。

        返回字段：
        - id / title（标题）/ content（Markdown 正文）/ articleAbstract（摘要）
        - type（1 原创 2 转载）/ reprintUrl（转载原文 url，原创为空）
        - groupingId + groupingName（分类）/ labelIds + labelNames（标签）/ specialId（专栏，可空）
        - auditState（1 审核中 2 已发布 3 审核不通过 4 草稿）/ updateTime（yyyy-MM-dd HH:mm:ss）""")
    public McpArticleDetailVo getArticle(
        @ToolParam(description = "文章 id（来自 search_articles 或 create_article 返回值）") Long articleId
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

    @Tool(name = "save_article_draft", description = """
        保存文章草稿（auditState=DRAFT，不进入审核、不触发 AI 摘要）。

        title/content 必传；不传 articleId 新建草稿，传 articleId 则覆盖保存已有草稿/文章。
        写完想直接发布用 create_article。""")
    public McpArticleSubmitVo saveArticleDraft(
        @ToolParam(description = "文章内容对象") McpArticleBo bo
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        checkTitleAndContent(bo);
        ArticleInformationTwoBo twoBo = new ArticleInformationTwoBo();
        twoBo.setId(bo.getArticleId());
        twoBo.setArticleTitle(bo.getTitle());
        twoBo.setArticleContent(bo.getContent());
        twoBo.setArticleAbstract(bo.getArticleAbstract());
        twoBo.setType(bo.getType());
        twoBo.setReprintUrl(bo.getReprintUrl());
        twoBo.setGroupingId(bo.getGroupingId());
        twoBo.setSpecialId(bo.getSpecialId());
        if (bo.getLabelIds() != null && !bo.getLabelIds().isEmpty()) {
            twoBo.setLabelId(bo.getLabelIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
        ArticleInformationVo vo = iArticleInformationService.saveDraft(twoBo);
        if (vo == null || vo.getId() == null || vo.getId() <= 0) {
            throw new ServiceException("草稿保存失败");
        }
        operLogRecorder.record("MCP文章", "保存文章草稿", BusinessType.INSERT,
            user.getUsername(), "id=" + vo.getId() + ", title=" + bo.getTitle(), null);
        return submitVo(vo.getId(), "DRAFT");
    }

    @Tool(name = "update_article", description = "更新已有文章并重新提交审核（auditState=UNDER_REVIEW），仅传需要修改的字段（未传字段保持不变）。articleId 必传，仅能更新自己的文章。")
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
        operLogRecorder.record("MCP文章", "更新文章", BusinessType.UPDATE,
            user.getUsername(), "id=" + bo.getArticleId() + ", title=" + bo.getTitle(), null);
        return submitVo(bo.getArticleId(), "UNDER_REVIEW");
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
        operLogRecorder.record("MCP文章", "删除文章", BusinessType.DELETE,
            user.getUsername(), "id=" + articleId, null);
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

    /**
     * 标题/正文非空校验：与前台发布校验（@NotBlank AddGroup/EditGroup）同语义，
     * MCP 直调 Service 不经过 Controller 参数校验，需在此兜底
     */
    private void checkTitleAndContent(McpArticleBo bo) {
        if (bo.getTitle() == null || bo.getTitle().isBlank()) {
            throw new ServiceException("title 不能为空");
        }
        if (bo.getContent() == null || bo.getContent().isBlank()) {
            throw new ServiceException("content 不能为空");
        }
    }

    /**
     * 日期入参解析：可空；格式错误明确报错（不静默忽略，避免 AI 以为条件已生效）
     */
    private LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException e) {
            throw new ServiceException("日期格式须为 yyyy-MM-dd：" + date);
        }
    }

    private McpArticleSubmitVo submitVo(Long articleId, String auditState) {
        McpArticleSubmitVo vo = new McpArticleSubmitVo();
        vo.setArticleId(articleId);
        vo.setAuditState(auditState);
        return vo;
    }
}
