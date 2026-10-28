package com.qixidi.mcp.domain.vo.timeNotes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小记条目（search_time_notes 列表行 / get_time_note 详情共用）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpTimeNoteItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 小记 id
     */
    private Long timeNoteId;

    /**
     * 标题
     */
    private String title;

    /**
     * 记录日期（yyyy-MM-dd）
     */
    private String recordTime;

    /**
     * 正文（Markdown）
     */
    private String content;
}
