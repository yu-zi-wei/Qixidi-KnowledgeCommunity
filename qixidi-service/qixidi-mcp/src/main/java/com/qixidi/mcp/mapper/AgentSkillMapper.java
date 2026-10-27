package com.qixidi.mcp.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.mcp.domain.entity.AgentSkill;
import com.qixidi.mcp.domain.vo.AgentSkillVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * agent 写作 skill
 *
 * @author zi-wei
 */
@Mapper
public interface AgentSkillMapper extends BaseMapperPlus<AgentSkillMapper, AgentSkill, AgentSkillVo> {
}
