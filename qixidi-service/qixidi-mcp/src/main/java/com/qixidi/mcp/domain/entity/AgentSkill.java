package com.qixidi.mcp.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.light.core.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * agent 写作 skill（两级：平台官方 userId=NULL + 用户自建）
 *
 * @author zi-wei
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("b_agent_skill")
public class AgentSkill extends BaseEntity {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属用户 uuid（NULL = 平台官方 skill，全局分发）
     */
    private String userId;

    /**
     * skill 名称
     */
    private String name;

    /**
     * skill 简介（agent 通过 list_skills 判断是否加载）
     */
    private String description;

    /**
     * skill 正文（Markdown，写作指令）
     */
    private String content;

    /**
     * 状态（1 启用 0 停用）
     */
    private Integer status;
}
