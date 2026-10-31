package com.qixidi.system.api.timeNotes;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.BusinessType;
import com.qixidi.auth.annotation.Log;
import com.qixidi.auth.annotation.LogModule;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesSearchBo;
import com.qixidi.business.domain.vo.TimeNotesAdminVo;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.service.TimeNotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * 小记后台管理（列表全量用户 / 删除含相册清理；getInfo 复用前台公开详情链路）
 *
 * @author ziwei
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/time/notes")
@LogModule("小记管理")
public class TimeNotesAdminController {

    private final TimeNotesService timeNotesService;

    /**
     * 查询小记列表（全量用户，标题模糊）
     */
    @SaCheckPermission("timeNotes:query")
    @PostMapping("/list")
    public TableDataInfo<TimeNotesAdminVo> list(@RequestBody TimeNotesSearchBo bo) {
        return timeNotesService.adminQueryPageList(bo, bo);
    }

    /**
     * 获取小记详细信息
     */
    @SaCheckPermission("timeNotes:query")
    @GetMapping("/{id}")
    public TimeNotesInfoVo getInfo(@PathVariable("id") Long id) {
        return timeNotesService.getInfo(id);
    }

    /**
     * 删除小记（逻辑删 + 相册清理）
     */
    @SaCheckPermission("timeNotes:remove")
    @Log(title = "删除小记", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public void remove(@PathVariable Long[] ids) {
        timeNotesService.deleteByIds(Arrays.asList(ids));
    }
}
