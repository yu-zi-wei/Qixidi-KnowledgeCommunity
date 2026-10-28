package com.qixidi.mcp.domain.bo.timeNotes;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小记多条件查询入参（MCP search_time_notes，仅限本人）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpTimeNoteQueryBo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ToolParam(required = false, description = "标题模糊关键词")
    private String title;

    @ToolParam(required = false, description = "正文模糊关键词")
    private String content;

    @ToolParam(required = false, description = "记录日期范围起，格式 yyyy-MM-dd")
    private String beginDate;

    @ToolParam(required = false, description = "记录日期范围止，格式 yyyy-MM-dd")
    private String endDate;
}
