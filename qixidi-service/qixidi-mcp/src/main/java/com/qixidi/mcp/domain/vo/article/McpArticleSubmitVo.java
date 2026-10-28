package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章提交结果（MCP create_article / update_article 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleSubmitVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章 id
     */
    private Long articleId;

    /**
     * 提交后审核状态（UNDER_REVIEW 审核中）
     */
    private String auditState;
}
