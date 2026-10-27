package com.qixidi.mcp.tools;

import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.mcp.domain.entity.AgentSkill;
import com.qixidi.mcp.domain.vo.McpSkillBriefVo;
import com.qixidi.mcp.service.AgentSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * skill 分发 tool：agent 开始写作前先 list_skills 浏览、再 get_skill 读取用户写作偏好与平台规范
 *
 * @author zi-wei
 */
@Component
@RequiredArgsConstructor
public class SkillTool {

    private final AgentSkillService agentSkillService;

    @Tool(name = "list_skills", description = "列出当前可用的写作 skill（平台官方 + 当前用户自建，含启用状态），返回 id/name/description。开始写作前先调用本工具，再按需用 get_skill 读取详情。")
    public List<McpSkillBriefVo> listSkills() {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        return agentSkillService.listForAgent(user.getUuid());
    }

    @Tool(name = "get_skill", description = "读取指定 skill 的完整正文（Markdown 写作指令、用户写作偏好或平台规范）。与 list_skills 配合：先 list 拿 id，再 get 拿内容。")
    public AgentSkill getSkill(
        @ToolParam(description = "skill id（来自 list_skills）") Long skillId
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // 官方行或本人行可读，其余拒绝（service 层校验）
        return agentSkillService.getForAgent(skillId, user.getUuid());
    }
}
