package com.qixidi.business.api.frontDesk;


import com.qixidi.business.domain.bo.FeedbackBo;
import com.qixidi.business.domain.vo.FeedbackStatusSumVo;
import com.qixidi.business.domain.vo.FeedbackVo;
import com.qixidi.business.service.IFeedbackService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 【前台】用户反馈管理
 *
 * @author aurora
 * @date 2023-04-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping
public class FdFeedbackController {

    private final IFeedbackService IFeedbackService;

    /**
     * 查询反馈状态总和-白名单
     *
     * @return
     */
    @GetMapping("/white/feedback/status/sum")
    public FeedbackStatusSumVo statusSum() {
        return IFeedbackService.statusSum();
    }

    /**
     * 查询用户反馈列表-白名单
     */
    @GetMapping("/white/feedback/list")
    public TableDataInfo<FeedbackVo> list(@Validated(QueryGroup.class) FeedbackBo bo, PageQuery pageQuery) {
        return IFeedbackService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取反馈详情-白名单
     */
    @GetMapping("/white/feedback/byId/{id}")
    public FeedbackVo queryById(@PathVariable("id") Long id) {
        return IFeedbackService.queryById(id);
    }

    /**
     * 新增用户反馈
     */
    @Log(title = "新增用户反馈", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/frontDesk/feedback/add")
    public void add(@Validated(AddGroup.class) @RequestBody FeedbackBo bo) {
        IFeedbackService.insertByBo(bo);
    }

    /**
     * 更新用户反馈
     */
    @Log(title = "更新用户反馈", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/frontDesk/feedback/update")
    public void update(@Validated(EditGroup.class) @RequestBody FeedbackBo bo) {
        IFeedbackService.updateByBo(bo);
    }

    /**
     * 修改用户反馈状态
     */
    @RepeatSubmit()
    @GetMapping("/frontDesk/feedback/update/status/{id}/{status}")
    public void updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        IFeedbackService.updateStatus(id, status);
    }

    /**
     * 删除用户反馈
     */
    @RepeatSubmit()
    @DeleteMapping("/frontDesk/feedback/delete/{ids}")
    public void delete(@PathVariable Long[] ids) {
        IFeedbackService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}
