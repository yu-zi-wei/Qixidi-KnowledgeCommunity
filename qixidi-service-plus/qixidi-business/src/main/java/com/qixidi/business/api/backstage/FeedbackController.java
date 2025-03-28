package com.qixidi.business.api.backstage;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.FeedbackBo;
import com.qixidi.business.domain.vo.FeedbackVo;
import com.qixidi.business.service.IFeedbackService;
import com.qixidi.auth.annotation.Log;
import com.light.redission.annotation.RepeatSubmit;
import com.qixidi.auth.api.BaseController;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.light.core.core.validate.QueryGroup;
import com.light.core.enums.BusinessType;
import com.light.excel.utils.ExcelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
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
    public R<FeedbackVo> getInfo(@PathVariable("id") Long id) {
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
    public R<Void> remove(@PathVariable Long[] ids) {
        return toAjax(IFeedbackService.deleteWithValidByIds(Arrays.asList(ids), true) ? 1 : 0);
    }
}

