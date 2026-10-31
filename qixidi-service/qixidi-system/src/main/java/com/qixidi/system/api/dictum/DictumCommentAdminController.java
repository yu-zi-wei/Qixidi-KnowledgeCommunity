package com.qixidi.system.api.dictum;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.business.domain.bo.dictum.DictumCommentBo;
import com.qixidi.business.domain.vo.dictum.DictumCommentVo;
import com.qixidi.business.service.dictum.DictumCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 随笔评论后台管理（全量平铺分页 / 删除跳过"仅本人"限制）
 *
 * @author ziwei
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/dictum/comment")
@LogModule("随笔评论")
public class DictumCommentAdminController {

    private final DictumCommentService dictumCommentService;

    /**
     * 查询随笔评论列表（全量，内容模糊 / 随笔 id / 状态筛选）
     */
    @SaCheckPermission("dictum:comment:query")
    @PostMapping("/list")
    public TableDataInfo<DictumCommentVo> list(@RequestBody DictumCommentBo bo) {
        return dictumCommentService.adminQueryPageList(bo, bo);
    }

    /**
     * 批量删除随笔评论（软删 + 通知记录清理）
     */
    @SaCheckPermission("dictum:comment:remove")
    @Log(title = "删除随笔评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        dictumCommentService.adminDelete(Arrays.asList(ids));
    }
}
