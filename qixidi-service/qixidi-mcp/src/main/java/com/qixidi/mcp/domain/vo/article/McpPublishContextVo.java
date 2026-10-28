package com.qixidi.mcp.domain.vo.article;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MCP get_publish_context 返回：agent 写文章/小记前所需的可选项上下文
 * （条目仅含 id+name 核心字段，id 分别对应 labelIds/groupingId/specialId 取值）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpPublishContextVo {

    /**
     * 可选标签（全量，按热度排序）
     */
    private List<McpLabelItemVo> labels;

    /**
     * 分类分组（文章所属分类）
     */
    private List<McpGroupingItemVo> groupings;

    /**
     * 当前用户的专栏
     */
    private List<McpSpecialItemVo> specials;

    /**
     * 笔名（用户昵称）
     */
    private String penName;
}
