package com.qixidi.mcp.domain.vo.timeNotes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小记搜索结果行（MCP search_time_notes；小记无详情工具，正文直接返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpTimeNoteSearchItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long timeNoteId;

    private String title;

    /** 记录日期 yyyy-MM-dd */
    private String recordTime;

    private String content;
}
