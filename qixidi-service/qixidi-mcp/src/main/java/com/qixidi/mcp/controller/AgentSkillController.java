package com.qixidi.mcp.controller;

import com.light.core.enums.BusinessType;
import com.light.core.enums.MsgEnums;
import com.light.exception.ServiceException;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserRoleEnums;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.mcp.domain.bo.skill.AgentSkillBo;
import com.qixidi.mcp.domain.entity.AgentSkill;
import com.qixidi.mcp.service.AgentSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户自建写作 skill 管理（前台，需登录；只能操作自己的行，写操作仅限创作者/管理员）。
 * 返回裸数据由 ResponseAdvice 统一包装
 *
 * @author zi-wei
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/agent/skill")
@LogModule("agent skill")
public class AgentSkillController {

    private final AgentSkillService agentSkillService;

    @Log(title = "新增agent skill", businessType = BusinessType.INSERT)
    @PostMapping
    public AgentSkill create(@Validated @RequestBody AgentSkillBo bo) {
        TripartiteUser user = requireCreator();
        return agentSkillService.createForUser(user.getUuid(), bo);
    }

    /**
     * 我的 skill 列表：官方 skill（userId=NULL，只读）置顶 + 本人自建（含停用）
     */
    @GetMapping("/list")
    public List<AgentSkill> list() {
        TripartiteUser user = requireUser();
        return agentSkillService.listForUser(user.getUuid());
    }

    @Log(title = "修改agent skill", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Validated @RequestBody AgentSkillBo bo) {
        TripartiteUser user = requireCreator();
        agentSkillService.updateForUser(id, user.getUuid(), bo);
    }

    @Log(title = "删除agent skill", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        TripartiteUser user = requireCreator();
        agentSkillService.deleteForUser(id, user.getUuid());
    }

    private TripartiteUser requireUser() {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录已过期");
        }
        return user;
    }

    /**
     * 与文章/小记发布、API key 创建同款创作者门槛：创作者或管理员
     */
    private TripartiteUser requireCreator() {
        TripartiteUser user = requireUser();
        if (!UserRoleEnums.getAdvancedRoleList().contains(user.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        return user;
    }
}
