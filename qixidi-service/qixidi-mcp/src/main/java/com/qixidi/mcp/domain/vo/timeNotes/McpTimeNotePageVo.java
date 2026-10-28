package com.qixidi.mcp.domain.vo.timeNotes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 小记分页结果（MCP search_time_notes 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpTimeNotePageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页小记列表
     */
    private List<McpTimeNoteSearchItemVo> rows;
}
