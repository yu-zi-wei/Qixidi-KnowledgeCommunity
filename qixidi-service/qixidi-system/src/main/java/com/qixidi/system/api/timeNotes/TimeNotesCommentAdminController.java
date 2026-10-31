package com.qixidi.system.api.timeNotes;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesCommentBo;
import com.qixidi.business.domain.vo.timeNotes.TimeNotesCommentVo;
import com.qixidi.business.service.timeNotes.TimeNotesCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 小记评论后台管理（全量平铺分页 / 删除跳过"仅本人"限制）
 *
 * @author ziwei
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/time/notes/comment")
@LogModule("小记评论")
public class TimeNotesCommentAdminController {

    private final TimeNotesCommentService timeNotesCommentService;

    /**
     * 查询小记评论列表（全量，内容模糊 / 小记 id / 状态筛选）
     */
    @SaCheckPermission("timeNotes:comment:query")
    @PostMapping("/list")
    public TableDataInfo<TimeNotesCommentVo> list(@RequestBody TimeNotesCommentBo bo) {
        return timeNotesCommentService.adminQueryPageList(bo, bo);
    }

    /**
     * 批量删除小记评论（软删 + 通知记录清理）
     */
    @SaCheckPermission("timeNotes:comment:remove")
    @Log(title = "删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        timeNotesCommentService.adminDelete(Arrays.asList(ids));
    }
}
