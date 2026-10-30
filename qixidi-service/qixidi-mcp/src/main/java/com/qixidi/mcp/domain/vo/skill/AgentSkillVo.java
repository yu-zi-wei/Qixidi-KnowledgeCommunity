package com.qixidi.mcp.domain.vo.skill;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import com.qixidi.mcp.domain.entity.AgentSkill;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
}
