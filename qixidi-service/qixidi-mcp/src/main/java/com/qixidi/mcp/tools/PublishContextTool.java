package com.qixidi.mcp.tools;

import com.light.core.core.domain.PageQuery;
import com.light.exception.ServiceException;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.label.LabelGroupingInfoBo;
import com.qixidi.business.domain.bo.special.SpecialInformationBo;
import com.qixidi.business.service.label.ILabelGroupingInfoService;
import com.qixidi.business.service.label.ILabelInfoService;
import com.qixidi.business.service.special.ISpecialInformationService;
import com.qixidi.mcp.domain.vo.article.McpGroupingItemVo;
import com.qixidi.mcp.domain.vo.article.McpLabelItemVo;
import com.qixidi.mcp.domain.vo.article.McpPublishContextVo;
import com.qixidi.mcp.domain.vo.article.McpSpecialItemVo;
import com.qixidi.mcp.domain.vo.special.McpSpecialSubmitVo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 发布上下文 tool：agent 写文章/小记前先调它拿可选标签、分类分组、专栏与笔名；
 * 支持新建专栏补充发布上下文
 *
 * @author zi-wei
 */
@Component
@RequiredArgsConstructor
public class PublishContextTool {

    private final ILabelInfoService iLabelInfoService;
    private final ILabelGroupingInfoService iLabelGroupingInfoService;
    private final ISpecialInformationService iSpecialInformationService;

    @Tool(name = "get_publish_context", description = """
        获取发布上下文，写作前调用。

        返回：
        - labels：可选标签 / groupings：分类分组 / specials：我的专栏（条目均为 {id, name}）
        - penName：笔名（用户昵称）

        create_article 等工具的 labelIds / groupingId / specialId 入参取值应来自本结果。""")
    public McpPublishContextVo getPublishContext() {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        McpPublishContextVo vo = new McpPublishContextVo();
        // 业务 VO → MCP 条目（id+name），剥离 isFollow/buttonLoading 等前台展示字段
        vo.setLabels(iLabelInfoService.systemLabel(null).stream().map(l -> {
            McpLabelItemVo item = new McpLabelItemVo();
            item.setId(l.getId());
            item.setName(l.getLabelName());
            return item;
        }).toList());
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageSize(100);
        vo.setGroupings(iLabelGroupingInfoService.fdkGroupingList(new LabelGroupingInfoBo(), pageQuery)
            .getRows().stream().map(g -> {
                McpGroupingItemVo item = new McpGroupingItemVo();
                item.setId(g.getId());
                item.setName(g.getGroupingName());
                return item;
            }).toList());
        SpecialInformationBo specialBo = new SpecialInformationBo();
        specialBo.setUid(user.getUuid());
        vo.setSpecials(iSpecialInformationService.specialListUid(specialBo).stream().map(s -> {
            McpSpecialItemVo item = new McpSpecialItemVo();
            item.setId(s.getId());
            item.setName(s.getSpecialName());
            return item;
        }).toList());
        vo.setPenName(user.getNickname());
        return vo;
    }

    @Tool(name = "create_special", description = "新建文章专栏，返回 specialId，可直接用于 create_article 的 specialId。专栏把同一系列的文章收纳在一起（如一个教程系列）。name/introduce 必传，与前台发布页创建专栏同链路。")
    public McpSpecialSubmitVo createSpecial(
        @ToolParam(description = "专栏名称") String name,
        @ToolParam(description = "专栏简介，一两句话说明专栏收录什么内容") String introduce,
        @ToolParam(description = "专栏封面图片 url（来自 upload_image 返回值），不传则无封面", required = false) String coverUrl
    ) {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        // 前台 @NotBlank(AddGroup) 同语义，MCP 直调 Service 须自行兜底
        if (name == null || name.isBlank()) {
            throw new ServiceException("name 不能为空");
        }
        if (introduce == null || introduce.isBlank()) {
            throw new ServiceException("introduce 不能为空");
        }
        SpecialInformationBo bo = new SpecialInformationBo();
        bo.setSpecialName(name.trim());
        bo.setSpecialIntroduce(introduce.trim());
        bo.setCover(coverUrl == null || coverUrl.isBlank() ? null : coverUrl.trim());
        // 属主 uid 由 service 在保存前从登录态强制回填，tool 层不传
        if (!iSpecialInformationService.insertByBo(bo) || bo.getId() == null) {
            throw new ServiceException("专栏创建失败");
        }
        McpSpecialSubmitVo vo = new McpSpecialSubmitVo();
        vo.setSpecialId(bo.getId());
        vo.setName(bo.getSpecialName());
        return vo;
    }
}
