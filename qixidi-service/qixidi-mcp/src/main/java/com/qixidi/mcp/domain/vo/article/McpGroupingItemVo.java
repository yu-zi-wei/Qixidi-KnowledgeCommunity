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

    private Long id;

    private String name;
}
