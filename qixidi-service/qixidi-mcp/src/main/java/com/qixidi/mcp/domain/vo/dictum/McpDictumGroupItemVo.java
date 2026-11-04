package com.qixidi.mcp.domain.vo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 随笔分类条目（MCP get_dictum_context.groups 列表行，分类全站共享）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumGroupItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分类 id
     */
    private Long groupId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类简介
     */
    private String briefIntroduction;
}
