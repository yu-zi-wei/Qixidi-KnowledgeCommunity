package com.qixidi.mcp.domain.vo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 随笔删除结果（MCP delete_dictum 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumDeleteVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 随笔 id
     */
    private Long dictumId;

    /**
     * 是否删除成功
     */
    private Boolean deleted;
}
