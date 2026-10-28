package com.qixidi.mcp.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.light.exception.ServiceException;
import com.qixidi.mcp.domain.bo.skill.AgentSkillBo;
import com.qixidi.mcp.domain.entity.AgentSkill;
import com.qixidi.mcp.domain.vo.skill.McpSkillBriefVo;
import com.qixidi.mcp.mapper.AgentSkillMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * agent 写作 skill：平台官方（userId=NULL）+ 用户自建 两级
 *
 * @author zi-wei
 */
@Service
@RequiredArgsConstructor
public class AgentSkillService {

    private final AgentSkillMapper skillMapper;

    /**
     * agent 侧列表：全局官方 skill + 该用户自建，按 id 升序
     */
    public List<McpSkillBriefVo> listForAgent(String uuid) {
        List<AgentSkill> list = skillMapper.selectList(
            Wrappers.<AgentSkill>lambdaQuery()
                .eq(AgentSkill::getStatus, 1)
                .and(w -> w.isNull(AgentSkill::getUserId).or().eq(AgentSkill::getUserId, uuid))
                .orderByAsc(AgentSkill::getId));
        return list.stream().map(s -> {
            McpSkillBriefVo vo = new McpSkillBriefVo();
            vo.setId(s.getId());
            vo.setName(s.getName());
            vo.setDescription(s.getDescription());
            return vo;
        }).toList();
    }

    /**
     * agent 侧读取全文：官方行或本用户行，否则拒绝
     */
    public AgentSkill getForAgent(Long id, String uuid) {
        AgentSkill skill = skillMapper.selectById(id);
        if (skill == null || skill.getStatus() != 1
            || (skill.getUserId() != null && !skill.getUserId().equals(uuid))) {
            throw new ServiceException("skill 不存在");
        }
        return skill;
    }

    /**
     * 用户侧列表：官方 skill（userId=NULL，只读展示）+ 自建（含停用），官方置顶。
     * 官方行的编辑/删除由 requireOwned 天然拒绝（uuid 不等于 NULL）
     */
    public List<AgentSkill> listForUser(String uuid) {
        return skillMapper.selectList(
            Wrappers.<AgentSkill>lambdaQuery()
                .and(w -> w.isNull(AgentSkill::getUserId).or().eq(AgentSkill::getUserId, uuid))
                .orderByAsc(AgentSkill::getUserId)
                .orderByDesc(AgentSkill::getId));
    }

    public AgentSkill createForUser(String uuid, AgentSkillBo bo) {
        AgentSkill skill = new AgentSkill();
        skill.setUserId(uuid);
        skill.setName(bo.getName());
        skill.setDescription(bo.getDescription());
        skill.setContent(bo.getContent());
        skill.setStatus(1);
        skillMapper.insert(skill);
        return skill;
    }

    public void updateForUser(Long id, String uuid, AgentSkillBo bo) {
        AgentSkill skill = requireOwned(id, uuid);
        skill.setName(bo.getName());
        skill.setDescription(bo.getDescription());
        skill.setContent(bo.getContent());
        skillMapper.updateById(skill);
    }

    public void deleteForUser(Long id, String uuid) {
        AgentSkill skill = requireOwned(id, uuid);
        skillMapper.deleteById(skill.getId());
    }

    private AgentSkill requireOwned(Long id, String uuid) {
        AgentSkill skill = skillMapper.selectById(id);
        if (skill == null || !uuid.equals(skill.getUserId())) {
            throw new ServiceException("skill 不存在");
        }
        return skill;
    }
}
