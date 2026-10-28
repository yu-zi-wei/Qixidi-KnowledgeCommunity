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

    /**
     * 文章 id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String articleAbstract;

    /**
     * 类型（1 原创 2 转载）
     */
    private Integer type;

    /**
     * 审核状态（1 审核中 2 已发布 3 审核不通过 4 草稿）
     */
    private Integer auditState;

    /**
     * 作者昵称
     */
    private String authorName;

    /**
     * 最近更新时间（yyyy-MM-dd HH:mm:ss）
     */
    private String updateTime;
}
