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
import com.qixidi.mcp.domain.vo.McpPublishContextVo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

/**
 * 发布上下文 tool：agent 写文章/小记前先调它拿可选标签、分类分组、专栏与笔名
 *
 * @author zi-wei
 */
@Component
@RequiredArgsConstructor
public class PublishContextTool {

    private final ILabelInfoService iLabelInfoService;
    private final ILabelGroupingInfoService iLabelGroupingInfoService;
    private final ISpecialInformationService iSpecialInformationService;

    @Tool(name = "get_publish_context", description = "获取发布上下文：写作前调用，返回当前用户可选的标签(labels)、分类分组(groupings)、专栏(specials)与笔名(penName)。create_article 等工具的 labelIds/groupingId/specialId 入参取值应来自本结果。")
    public McpPublishContextVo getPublishContext() {
        TripartiteUser user = LoginHelper.getTripartiteUser();
        if (user == null) {
            throw new ServiceException("登录状态无效，请检查 API key");
        }
        McpPublishContextVo vo = new McpPublishContextVo();
        vo.setLabels(iLabelInfoService.systemLabel(null));
        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageSize(100);
        vo.setGroupings(iLabelGroupingInfoService.fdkGroupingList(new LabelGroupingInfoBo(), pageQuery).getRows());
        SpecialInformationBo specialBo = new SpecialInformationBo();
        specialBo.setUid(user.getUuid());
        vo.setSpecials(iSpecialInformationService.specialListUid(specialBo));
        vo.setPenName(user.getNickname());
        return vo;
    }
}
