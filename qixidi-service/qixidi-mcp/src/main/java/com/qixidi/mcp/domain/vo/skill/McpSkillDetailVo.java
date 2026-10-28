package com.qixidi.mcp.domain.vo.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * skill 详情（MCP get_skill 返回，不含 userId 等内部字段）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class McpSkillDetailVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * skill 名称
     */
    private String name;

    /**
     * skill 简介
     */
    private String description;

    /**
     * skill 正文（Markdown 写作指令）
     */
    private String content;
}
