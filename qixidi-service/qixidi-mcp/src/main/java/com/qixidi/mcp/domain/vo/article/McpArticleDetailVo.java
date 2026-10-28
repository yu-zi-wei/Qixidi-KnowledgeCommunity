package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章详情（MCP get_article 返回，仅暴露 agent 编辑所需核心字段）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * Markdown 正文
     */
    private String content;

    /**
     * 摘要
     */
    private String articleAbstract;

    /**
     * 文章类型：1 原创 2 转载
     */
    private Integer type;

    /**
     * 转载原文链接
     */
    private String reprintUrl;

    /**
     * 分类分组 id
     */
    private Long groupingId;

    /**
     * 分类分组名称
     */
    private String groupingName;

    /**
     * 标签 id 列表
     */
    private List<Long> labelIds;

    /**
     * 标签名称列表
     */
    private List<String> labelNames;

    /**
     * 专栏 id（未投专栏为 null）
     */
    private Long specialId;

    /**
     * 审核状态：1 审核中、2 审核通过、3 审核不通过、4 草稿
     */
    private Integer auditState;

    /**
     * 最后更新时间（yyyy-MM-dd HH:mm:ss）
     */
    private String updateTime;
}
