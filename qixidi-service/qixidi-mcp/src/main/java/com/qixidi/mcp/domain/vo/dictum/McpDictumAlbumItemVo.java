package com.qixidi.mcp.domain.vo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 随笔专辑条目（MCP get_dictum_context.albums 列表行，专辑归本人）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumAlbumItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 专辑 id
     */
    private Long albumId;

    /**
     * 专辑名称
     */
    private String name;

    /**
     * 专辑简介
     */
    private String briefIntroduction;

    /**
     * 专辑可见状态：1 公开、2 私有、3 关注可看
     */
    private Integer albumState;
}
