package com.qixidi.business.api.frontDesk.timeNotes;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesSearchBo;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.TimeNotesVo;
import com.qixidi.business.service.TimeNotesService;
import lombok.RequiredArgsConstructor;
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
    public TimeNotesInfoVo getInfo(@PathVariable("id") Long id) {
        return timeNotesService.getInfo(id);
    }


    /**
     * 获取时光小计列表
     *
     * @param bo
     */
    @PostMapping("/list")
    public TableDataInfo<TimeNotesVo> list(@RequestBody TimeNotesSearchBo bo) {
        PageQuery query = bo;
        return timeNotesService.list(bo, query.build());
    }

    /**
     * 小记归档
     *
     * @return
     */
    @PostMapping("/archive")
    public TableDataInfo<TimeNotesVo> archive(@RequestBody TimeNotesSearchBo bo) {
        PageQuery query = bo;
        String tripartiteUuid = LoginHelper.getTripartiteUuid();
        if (tripartiteUuid != null) {
            bo.setUid(Long.valueOf(tripartiteUuid));
        }
        return timeNotesService.list(bo, query.build());
    }
}
