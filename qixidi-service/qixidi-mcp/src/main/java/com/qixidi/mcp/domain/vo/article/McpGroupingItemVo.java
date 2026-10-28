package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 分类分组条目（get_publish_context.groupings 行；id 用于 create_article 的 groupingId 取值）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpGroupingItemVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 分组 id（写入文章的 groupingId）
     */
    private Long id;

    /**
     * 分组名（文章所属分类）
     */
    private String name;
}
