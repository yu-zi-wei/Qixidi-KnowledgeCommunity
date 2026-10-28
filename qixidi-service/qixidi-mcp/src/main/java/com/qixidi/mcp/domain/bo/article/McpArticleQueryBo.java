package com.qixidi.mcp.domain.bo.article;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 文章多条件查询入参（MCP search_articles）。
 * 数据范围：不传 authorName 查本人全部状态；传 authorName 查该作者已发布文章
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpArticleQueryBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ToolParam(required = false, description = "关键词，同时模糊匹配标题和摘要（与前台搜索一致）")
    private String title;

    @ToolParam(required = false, description = "摘要模糊关键词，仅匹配摘要")
    private String articleAbstract;

    @ToolParam(required = false, description = "审核状态多选：1 审核中、2 已发布、3 审核不通过、4 草稿。仅查本人时生效（跨作者查询强制为已发布）")
    private List<Integer> auditStates;

    @ToolParam(required = false, description = "文章类型：1 原创、2 转载")
    private Integer type;

    @ToolParam(required = false, description = "更新时间范围起，格式 yyyy-MM-dd")
    private String beginDate;

    @ToolParam(required = false, description = "更新时间范围止，格式 yyyy-MM-dd")
    private String endDate;

    @ToolParam(required = false, description = "作者昵称模糊关键词。不传查本人（含草稿等全部状态）；传则查该作者的已发布文章")
    private String authorName;

    @ToolParam(required = false, description = "专栏 id（来自 get_publish_context.specials）")
    private Long specialId;
}
