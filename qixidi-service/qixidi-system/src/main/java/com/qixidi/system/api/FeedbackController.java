package com.qixidi.system.api;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.qixidi.business.domain.bo.FeedbackBo;
import com.qixidi.business.domain.vo.FeedbackVo;
import com.qixidi.business.service.IFeedbackService;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.light.redission.annotation.RepeatSubmit;

import com.light.core.core.domain.PageQuery;
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
@RequestMapping("/system/feedback")
@LogModule("用户反馈")
public class FeedbackController {

    private final IFeedbackService IFeedbackService;

    /**
     * 查询用户反馈列表
     */
    @PostMapping("/list")
    public TableDataInfo<FeedbackVo> list(@Validated(QueryGroup.class) @RequestBody FeedbackBo bo) {
        return IFeedbackService.queryPageList(bo, bo);
    }

    /**
     * 导出用户反馈列表
     */
    @SaCheckPermission("feedback:export")
    @Log(title = "导出用户反馈", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(@Validated FeedbackBo bo, HttpServletResponse response) {
        List<FeedbackVo> list = IFeedbackService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户反馈", FeedbackVo.class, response);
    }

    /**
     * 获取用户反馈详细信息
     */
    @SaCheckPermission("feedback:query")
    @GetMapping("/{id}")
    public FeedbackVo getInfo(@PathVariable("id") Long id) {
        return IFeedbackService.queryById(id);
    }

    /**
     * 新增用户反馈
     */
    @SaCheckPermission("feedback:add")
    @Log(title = "新增用户反馈", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public void add(@Validated(AddGroup.class) @RequestBody FeedbackBo bo) {
        IFeedbackService.insertByBo(bo);
    }

    /**
     * 修改用户反馈
     */
    @SaCheckPermission("feedback:edit")
    @Log(title = "修改用户反馈", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public void edit(@Validated(EditGroup.class) @RequestBody FeedbackBo bo) {
        IFeedbackService.updateByBo(bo);
    }

    /**
     * 删除用户反馈
     */
    @SaCheckPermission("feedback:remove")
    @Log(title = "删除用户反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        IFeedbackService.deleteWithValidByIds(Arrays.asList(ids), true);
    }
}

