package com.qixidi.mcp.tools;

import cn.hutool.json.JSONUtil;
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
}
