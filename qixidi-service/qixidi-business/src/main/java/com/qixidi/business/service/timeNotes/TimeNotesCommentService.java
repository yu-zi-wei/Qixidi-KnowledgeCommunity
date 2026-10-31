package com.qixidi.business.service.timeNotes;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesCommentBo;
import com.qixidi.business.domain.vo.timeNotes.TimeNotesCommentVo;

import java.util.List;

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

    /**
     * 后台全量分页（不区分楼层，平铺；填充评论人/回复对象/所在小记标题）
     *
     * @param bo        查询条件（timeNotesId eq / content like / status eq）
     * @param pageQuery 分页参数
     * @return 评论分页
     */
    TableDataInfo<TimeNotesCommentVo> adminQueryPageList(TimeNotesCommentBo bo, PageQuery pageQuery);

    /**
     * 后台批量删除（软删，跳过"仅本人"限制；同步清理评论产生的通知记录）
     *
     * @param ids 评论 id 集合
     */
    void adminDelete(List<Long> ids);
}
