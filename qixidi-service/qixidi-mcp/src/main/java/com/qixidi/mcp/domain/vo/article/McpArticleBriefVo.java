package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文章列表项（MCP list_my_articles 返回行）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleBriefVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 审核状态：1 审核中、2 审核通过、3 审核不通过、4 草稿
     */
    private Integer auditState;

    /**
     * 最后更新时间（yyyy-MM-dd HH:mm:ss）
     */
    private String updateTime;
}
