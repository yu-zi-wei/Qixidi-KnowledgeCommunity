package com.qixidi.mcp.domain.bo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.Serial;
import java.io.Serializable;

/**
 * 随笔多条件查询入参（MCP search_dictums，仅限本人）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumQueryBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ToolParam(required = false, description = "内容模糊关键词")
    private String content;

    @ToolParam(required = false, description = "标签模糊关键词（单个标签）")
    private String label;

    @ToolParam(required = false, description = "分类 id 精确过滤，取值来自 get_dictum_context.groups")
    private Long groupId;

    @ToolParam(required = false, description = "专辑 id 精确过滤，取值来自 get_dictum_context.albums")
    private Long albumId;

    @ToolParam(required = false, description = "出处作者精确过滤（如：鲁迅）")
    private String author;

    @ToolParam(required = false, description = "可见状态过滤：1 公开、2 私有、3 关注可看")
    private Integer dictumState;
}
