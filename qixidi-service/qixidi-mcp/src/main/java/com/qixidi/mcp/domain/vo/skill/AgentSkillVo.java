package com.qixidi.mcp.domain.vo.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * skill 视图
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class AgentSkillVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 所属用户 uuid（NULL = 平台官方 skill）
     */
    private String userId;

    /**
     * skill 名称
     */
    private String name;

    /**
     * skill 简介
     */
    private String description;

    /**
     * skill 正文（Markdown）
     */
    private String content;

    /**
     * 状态（1 启用 0 停用）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
