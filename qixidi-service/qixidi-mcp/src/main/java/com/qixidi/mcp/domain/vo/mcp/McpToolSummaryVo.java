package com.qixidi.mcp.domain.vo.mcp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * MCP tool 清单条目（/agent/mcp-info/tools，管理页"工具清单"展示）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpToolSummaryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 工具名（snake_case）
     */
    private String name;

    /**
     * 工具描述（多行）
     */
    private String description;
}
