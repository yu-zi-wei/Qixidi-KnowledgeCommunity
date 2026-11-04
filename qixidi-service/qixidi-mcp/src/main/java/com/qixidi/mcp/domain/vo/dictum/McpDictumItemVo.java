package com.qixidi.mcp.domain.vo.dictum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 随笔条目（MCP search_dictums 列表行）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpDictumItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 随笔 id
     */
    private Long dictumId;

    /**
     * 内容
     */
    private String content;

    /**
     * 分类 id
     */
    private Long groupId;

    /**
     * 分类名称
     */
    private String groupName;

    /**
     * 专辑 id（未归专辑为空）
     */
    private Long albumId;

    /**
     * 专辑名称（未归专辑为空）
     */
    private String albumName;

    /**
     * 标签列表
     */
    private List<String> labelList;

    /**
     * 出处作者
     */
    private String author;

    /**
     * 出处作品名
     */
    private String worksName;

    /**
     * 可见状态：1 公开、2 私有、3 关注可看
     */
    private Integer dictumState;

    /**
     * 更新时间（yyyy-MM-dd HH:mm:ss）
     */
    private String updateTime;
}
