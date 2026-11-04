package com.qixidi.mcp.domain.vo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 随笔提交结果（MCP create_dictum / update_dictum 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumSubmitVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 随笔 id
     */
    private Long dictumId;

    /**
     * 操作结果（created / updated）
     */
    private String status;
}
