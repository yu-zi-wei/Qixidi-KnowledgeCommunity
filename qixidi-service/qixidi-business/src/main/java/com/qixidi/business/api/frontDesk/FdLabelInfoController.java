package com.qixidi.business.api.frontDesk;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.QueryGroup;
import com.qixidi.business.domain.bo.label.LabelGroupingInfoBo;
import com.qixidi.business.domain.bo.label.LabelInfoBo;
import com.qixidi.business.domain.vo.label.LabelGroupingInfoVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.service.label.ILabelGroupingInfoService;
import com.qixidi.business.service.label.ILabelInfoService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 【前台-白名单】标签信息管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/white/label")
public class FdLabelInfoController {

    private final ILabelInfoService iLabelInfoService;

    private final ILabelGroupingInfoService iLabelGroupingInfoService;

    /**
     * 查询标签信息列表
     */
    @GetMapping("/list")
    public List<LabelInfoVo> fdLabelList(LabelInfoBo bo) {
        return iLabelInfoService.fdLabelList(bo);
    }


    /**
     * 查询标签详情
     */
    @GetMapping("/info/{id}")
     public LabelInfoVo fdLabelInfo(@PathVariable("id") Long id) {
     return iLabelInfoService.fdLabelInfo(id);
     }

     /**
     * 查询标签分组详情
     */
    @GetMapping("/grouping/info/{id}")
    public LabelGroupingInfoVo LabelGroupingInfo(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return iLabelInfoService.LabelGroupingInfo(id);
    }


    /**
     * 查询标签分组信息列表
     */
    @GetMapping("/grouping/list")
    public TableDataInfo<LabelGroupingInfoVo> fdkGroupingList(@Validated(QueryGroup.class) LabelGroupingInfoBo bo, PageQuery pageQuery) {
        return iLabelGroupingInfoService.fdkGroupingList(bo, pageQuery);
    }

}
