package com.aurora.web.controller.frontDesk;


import com.aurora.business.domain.bo.FeedbackBo;
import com.aurora.business.domain.vo.FeedbackStatusSumVo;
import com.aurora.business.domain.vo.FeedbackVo;
import com.aurora.business.service.IFeedbackService;
import com.aurora.common.annotation.Log;
import com.aurora.common.annotation.RepeatSubmit;
import com.aurora.common.core.controller.BaseController;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.aurora.common.core.validate.QueryGroup;
import com.aurora.common.enums.BusinessType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;

/**
 * 用户反馈Controller
 *
 * @author aurora
 * @date 2023-04-17
 */
@Validated
@Api(value = "用户反馈控制器", tags = {"用户反馈管理"})
@RequiredArgsConstructor
@RestController
@RequestMapping
public class FdFeedbackController extends BaseController {

    private final IFeedbackService IFeedbackService;

    @ApiOperation("查询用户状态总和")
    @GetMapping("/white/feedback/status/sum")
    public R<FeedbackStatusSumVo> statusSum() {
        return R.ok(IFeedbackService.statusSum());
    }

    /**
     * 查询用户反馈列表
     */
    @ApiOperation("查询用户反馈列表")
    @GetMapping("/white/feedback/list")
    public TableDataInfo<FeedbackVo> list(@Validated(QueryGroup.class) FeedbackBo bo, PageQuery pageQuery) {
        return IFeedbackService.queryPageList(bo, pageQuery);
    }

    /**
     * 获取反馈详情
     */
    @ApiOperation("获取反馈详情")
    @GetMapping("/white/feedback/byId/{id}")
    public R<FeedbackVo> queryById(@PathVariable("id") Long id) {
        return R.ok(IFeedbackService.queryById(id));
    }

    /**
     * 新增用户反馈
     */
    @ApiOperation("新增用户反馈")
    @Log(title = "新增用户反馈", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/frontDesk/feedback/add")
    public R<Void> add(@Validated(AddGroup.class) @RequestBody FeedbackBo bo) {
        return toAjax(IFeedbackService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 更新用户反馈
     */
    @ApiOperation("更新用户反馈")
    @Log(title = "更新用户反馈", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/frontDesk/feedback/update")
    public R<Void> update(@Validated(EditGroup.class) @RequestBody FeedbackBo bo) {
        return toAjax(IFeedbackService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户反馈状态
     */
    @ApiOperation("更新用户反馈状态")
    @RepeatSubmit()
    @GetMapping("/frontDesk/feedback/update/status/{id}/{status}")
    public R<Void> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        return toAjax(IFeedbackService.updateStatus(id, status) ? 1 : 0);
    }

    /**
     * 删除用户反馈
     */
    @ApiOperation("删除用户反馈")
    @RepeatSubmit()
    @DeleteMapping("/frontDesk/feedback/delete/{ids}")
    public R<Void> delete(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(IFeedbackService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}
