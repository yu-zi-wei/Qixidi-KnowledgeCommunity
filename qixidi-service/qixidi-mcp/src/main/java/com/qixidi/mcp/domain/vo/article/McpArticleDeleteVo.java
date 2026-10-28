package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章删除结果（MCP delete_article 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleDeleteVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 文章 id
     */
    private Long articleId;

    /**
     * 是否删除成功
     */
    private Boolean deleted;
}
