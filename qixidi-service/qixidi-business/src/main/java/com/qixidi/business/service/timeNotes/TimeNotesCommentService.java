package com.qixidi.business.service.timeNotes;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesCommentBo;
import com.qixidi.business.domain.vo.timeNotes.TimeNotesCommentVo;

/**
 * 时光小记评论 Service
 *
 * @author zi-wei
 * @date 2026-09-15
 */
public interface TimeNotesCommentService {

    /**
     * 新增评论
     *
     * @param bo 评论信息
     * @return 新增后的评论（含后端生成的 id，供前端落定乐观更新节点）
     */
    TimeNotesCommentVo add(TimeNotesCommentBo bo);

    /**
     * 删除评论（软删，仅本人可删）
     *
     * @param id 评论 id
     */
    void delete(Long id);

    /**
     * 获取评论列表（分页，楼层结构）
     *
     * @param id        时光小记 id
     * @param pageQuery 分页参数
     * @return 楼层结构评论列表
     */
    TableDataInfo<TimeNotesCommentVo> commentList(Long id, PageQuery pageQuery);
}
