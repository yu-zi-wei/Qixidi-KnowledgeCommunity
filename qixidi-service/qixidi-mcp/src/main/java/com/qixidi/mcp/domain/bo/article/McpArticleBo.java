package com.qixidi.mcp.domain.bo.article;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章创建/更新入参对象（MCP create_article / update_article 共用）
 *
 * Spring AI 对 POJO 工具参数递归生成 object schema；字段描述走
 * com.fasterxml.jackson.annotation.JsonPropertyDescription（Jackson 2/3 共用层）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonPropertyDescription("文章标题")
    private String title;

    @JsonPropertyDescription("Markdown 正文")
    private String content;

    @JsonPropertyDescription("标签 id 列表（来自 get_publish_context.labels）")
    private List<Long> labelIds;

    @JsonPropertyDescription("分类分组 id（来自 get_publish_context.groupings）")
    private Long groupingId;

    @JsonPropertyDescription("专栏 id（来自 get_publish_context.specials，不投专栏可省略）")
    private Long specialId;

    @JsonPropertyDescription("文章类型：1 原创 2 转载，默认 1")
    private Integer type;

    @JsonPropertyDescription("转载原文链接（type=2 时必填）")
    private String reprintUrl;

    @JsonPropertyDescription("自定义摘要（不填且 abstractSelect=true 时由系统 AI 生成）")
    private String articleAbstract;

    @JsonPropertyDescription("是否由系统 AI 生成摘要：true 且未传摘要时触发生成；默认 false")
    private Boolean abstractSelect;

    @JsonPropertyDescription("文章 id：create_article 更新已有文章时传；update_article 必传")
    private Long articleId;
}
