package com.qixidi.business.api.frontDesk.timeNotes;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesBo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesSearchBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.service.TimeNotesService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 时光小计
 *
 * @author zi-wei
 * @create 2025/3/26 10:10
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/frontDesk/time/notes")
public class TimeNotesController {

    private final TimeNotesService timeNotesService;

    /**
     * 创建时光小计
     *
     * @param bo
     */
    @PostMapping("/add")
    public R<Void> add(@RequestBody @Validated TimeNotesBo bo) {
        timeNotesService.add(bo);
        return R.ok();
    }

    /**
     * 更新时光小计
     *
     * @param bo
     */
    @PostMapping("/update")
    public R<Void> update(@RequestBody @Validated TimeNotesBo bo) {
        timeNotesService.update(bo);
        return R.ok();
    }

    /**
     * 删除时光小计
     *
     * @param id
     */
    @GetMapping("/delete/{id}")
    public R<Void> delete(@PathVariable("id") Long id) {
        timeNotesService.delete(id);
        return R.ok();
    }

    /**
     * 获取时光小计详情
     *
     * @param id
     * @return
     */
    @GetMapping("/getInfo/{id}")
    public R<TimeNotesInfoVo> getInfo(@PathVariable("id") Long id) {
        return R.ok(timeNotesService.getInfo(id));
    }


    /**
     * 获取时光小计列表
     *
     * @param bo
     */
    @PostMapping("/list")
    public TableDataInfo<TimeNotes> queryList(@RequestBody TimeNotesSearchBo bo) {
        PageQuery pageQuery = bo;
        return timeNotesService.queryList(bo, pageQuery.build());
    }

}
