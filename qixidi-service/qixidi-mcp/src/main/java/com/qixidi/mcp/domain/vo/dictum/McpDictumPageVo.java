package com.qixidi.mcp.domain.vo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 随笔分页结果（MCP search_dictums 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumPageVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总条数
     */
    private Long total;

    /**
     * 当前页随笔列表
     */
    private List<McpDictumItemVo> rows;
}
