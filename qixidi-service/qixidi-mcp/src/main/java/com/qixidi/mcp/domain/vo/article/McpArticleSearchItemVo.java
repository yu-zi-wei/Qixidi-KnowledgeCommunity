package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章搜索结果行（MCP search_articles，仅元数据不含正文，正文用 get_article 获取）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleSearchItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private String articleAbstract;

    /** 1 原创 2 转载 */
    private Integer type;

    /** 1 审核中 2 已发布 3 审核不通过 4 草稿 */
    private Integer auditState;

    private String authorName;

    private String updateTime;
}
