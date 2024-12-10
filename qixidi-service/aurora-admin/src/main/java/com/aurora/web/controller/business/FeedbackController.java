package com.aurora.web.controller.business;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.aurora.business.domain.bo.FeedbackBo;
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
import com.aurora.common.utils.poi.ExcelUtil;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户反馈管理
 *
 * @author aurora
 * @date 2023-04-17
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/Feedback")
public class FeedbackController extends BaseController {

    private final IFeedbackService IFeedbackService;

    /**
     * 查询用户反馈列表
     */
    @SaCheckPermission("business:Feedback:list")
    @GetMapping("/list")
    public TableDataInfo<FeedbackVo> list(@Validated(QueryGroup.class) FeedbackBo bo, PageQuery pageQuery) {
        return IFeedbackService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户反馈列表
     */
    @SaCheckPermission("business:Feedback:export")
    @Log(title = "用户反馈", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated FeedbackBo bo, HttpServletResponse response) {
        List<FeedbackVo> list = IFeedbackService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户反馈", FeedbackVo.class, response);
    }

    /**
     * 获取用户反馈详细信息
     */
    @SaCheckPermission("business:Feedback:query")
    @GetMapping("/{id}")
    public R<FeedbackVo> getInfo(@ApiParam("主键")
                                 @NotNull(message = "主键不能为空")
                                 @PathVariable("id") Long id) {
        return R.ok(IFeedbackService.queryById(id));
    }

    /**
     * 新增用户反馈
     */
    @SaCheckPermission("business:Feedback:add")
    @Log(title = "用户反馈", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody FeedbackBo bo) {
        return toAjax(IFeedbackService.insertByBo(bo) ? 1 : 0);
    }

    /**
     * 修改用户反馈
     */
    @SaCheckPermission("system:Feedback:edit")
    @Log(title = "用户反馈", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody FeedbackBo bo) {
        return toAjax(IFeedbackService.updateByBo(bo) ? 1 : 0);
    }

    /**
     * 删除用户反馈
     */
    @SaCheckPermission("business:Feedback:remove")
    @Log(title = "用户反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@ApiParam("主键串")
                          @NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(IFeedbackService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

