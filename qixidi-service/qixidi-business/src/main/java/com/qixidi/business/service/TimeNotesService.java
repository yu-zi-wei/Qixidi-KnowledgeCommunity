package com.qixidi.business.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesBo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesSearchBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.vo.TimeNotesAdminVo;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.TimeNotesVo;

import java.time.LocalDate;

/**
 * @author zi-wei
 * @create 2025/3/26 10:09
 */
public interface TimeNotesService {
    Long add(TimeNotesBo bo);

    void update(TimeNotesBo bo);

    void delete(Long id);

    TableDataInfo<TimeNotesVo> list(TimeNotesSearchBo bo, Page<TimeNotes> build);

    TimeNotesInfoVo getInfo(Long id);

    TableDataInfo<TimeNotes> queryList(TimeNotesSearchBo bo, Page<TimeNotes> build);

    /**
     * MCP agent 多条件分页查询：标题/正文模糊 + 记录日期范围，强制限定 uid
     */
    Page<TimeNotes> searchForAgent(String uid, String title, String content,
                                   LocalDate beginDate, LocalDate endDate, Page<TimeNotes> page);

    /**
     * 后台管理列表：全量用户（不限定登录态 uid），标题模糊 + uid 精确可选，fill 作者昵称/头像
     */
    TableDataInfo<TimeNotesAdminVo> adminQueryPageList(TimeNotesSearchBo bo, PageQuery pageQuery);

    /**
     * 后台批量删除（逻辑删 + 相册清理；后台链路无前台会话，跳过属主校验）
     */
    void deleteByIds(java.util.List<Long> ids);
}
