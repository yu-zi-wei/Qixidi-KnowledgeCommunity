package com.qixidi.business.comtroller.frontDesk.timeNotes;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.TimeNotesBo;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.TimeNotesVo;
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
@RequestMapping("/white/time/notes")
public class TimeNotesWhiteController {

    private final TimeNotesService timeNotesService;
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
     * @param pageQuery
     */
    @GetMapping("/list")
    public TableDataInfo<TimeNotesVo> list(PageQuery pageQuery) {
        return timeNotesService.list(pageQuery.build());
    }

}
