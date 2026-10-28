package com.qixidi.mcp.domain.bo.skill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * skill 创建/编辑入参
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class AgentSkillBo {

    /**
     * skill 名称
     */
    @NotBlank(message = "name不能为空")
    @Size(max = 100, message = "name长度不能超过100")
    private String name;

    /**
     * skill 简介（agent 通过 list_skills 判断是否加载）
     */
    @Size(max = 500, message = "description长度不能超过500")
    private String description;

    /**
     * skill 正文（Markdown，写作指令）
     */
    @NotBlank(message = "content不能为空")
    private String content;
}
