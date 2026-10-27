package com.qixidi.mcp.domain.vo;

import com.qixidi.business.domain.vo.label.LabelGroupingInfoVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.domain.vo.special.SpecialInformationVo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * MCP get_publish_context 返回：agent 写文章/小记前所需的可选项上下文
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpPublishContextVo {

    /**
     * 可选标签（全量，按热度排序）
     */
    private List<LabelInfoVo> labels;

    /**
     * 分类分组（文章所属分类）
     */
    private List<LabelGroupingInfoVo> groupings;

    /**
     * 当前用户的专栏
     */
    private List<SpecialInformationVo> specials;

    /**
     * 笔名（用户昵称）
     */
    private String penName;
}
