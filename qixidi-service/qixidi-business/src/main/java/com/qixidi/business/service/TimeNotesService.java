package com.qixidi.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesBo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesSearchBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.TimeNotesVo;

/**
 * @author zi-wei
 * @create 2025/3/26 10:09
 */
public interface TimeNotesService {
    void add(TimeNotesBo bo);

    void update(TimeNotesBo bo);

    void delete(Long id);

    TableDataInfo<TimeNotesVo> list(TimeNotesSearchBo bo, Page<TimeNotes> build);

    TimeNotesInfoVo getInfo(Long id);

    TableDataInfo<TimeNotes> queryList(TimeNotesSearchBo bo, Page<TimeNotes> build);
}
