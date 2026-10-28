package com.qixidi.mcp.domain.bo.article;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章内容入参对象（MCP create_article / update_article / save_article_draft 共用）
 *
 * Spring AI 对 POJO 工具参数递归生成 object schema：
 * 字段描述与必填性统一走 @ToolParam——字段默认 required（SpringAiSchemaModule
 * requiredByDefault=true），可选字段必须显式 required = false，否则 schema 的
 * required 数组会全量误导调用方
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ToolParam(required = false, description = "文章标题。create_article / save_article_draft 必填；update_article 不修改标题可不传")
    private String title;

    @ToolParam(required = false, description = "Markdown 正文。create_article / save_article_draft 必填；update_article 不修改正文可不传")
    private String content;

    @ToolParam(required = false, description = "标签 id 列表，取值来自 get_publish_context.labels。create_article 必填；update_article / save_article_draft 可不传")
    private List<Long> labelIds;

    @ToolParam(required = false, description = "分类分组 id，取值来自 get_publish_context.groupings。create_article 必填；update_article / save_article_draft 可不传")
    private Long groupingId;

    @ToolParam(required = false, description = "专栏 id，取值来自 get_publish_context.specials；不投专栏不传")
    private Long specialId;

    @ToolParam(required = false, description = "文章类型：1 原创（默认）、2 转载")
    private Integer type;

    @ToolParam(required = false, description = "转载原文链接，type=2 时必填")
    private String reprintUrl;

    @ToolParam(required = false, description = "自定义摘要，最长 200 字；不传且 abstractSelect=true 时由系统 AI 生成")
    private String articleAbstract;

    @ToolParam(required = false, description = "是否由系统 AI 生成摘要：true 且未传摘要时触发生成；默认 false。草稿不触发")
    private Boolean abstractSelect;

    @ToolParam(required = false, description = "文章 id：更新已有文章/草稿时必传（id 来自 search_articles 返回值），新建不传")
    private Long articleId;
}
