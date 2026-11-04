package com.qixidi.mcp.domain.vo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 随笔发布上下文（MCP get_dictum_context 返回）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumContextVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 随笔分类列表（全站共享）
     */
    private List<McpDictumGroupItemVo> groups;

    /**
     * 我的随笔专辑列表
     */
    private List<McpDictumAlbumItemVo> albums;
}
