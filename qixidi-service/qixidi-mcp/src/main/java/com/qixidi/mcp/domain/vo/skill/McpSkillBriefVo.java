package com.qixidi.mcp.domain.vo.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * skill 摘要（MCP list_skills 返回项，不含正文）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpSkillBriefVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * skill 名称
     */
    private String name;

    /**
     * skill 简介（agent 据此判断是否 get_skill 加载全文）
     */
    private String description;
}
