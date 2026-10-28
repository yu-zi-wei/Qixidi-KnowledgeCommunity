package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章分页结果（MCP search_articles 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticlePageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页文章列表
     */
    private List<McpArticleSearchItemVo> rows;
}
