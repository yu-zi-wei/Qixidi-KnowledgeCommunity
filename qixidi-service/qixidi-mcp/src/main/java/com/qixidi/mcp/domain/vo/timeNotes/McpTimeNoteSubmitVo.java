package com.qixidi.mcp.domain.vo.timeNotes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 小记提交结果（MCP create_time_note / update_time_note 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpTimeNoteSubmitVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 小记 id
     */
    private Long timeNoteId;

    /**
     * 操作结果（created / updated）
     */
    private String status;
}
