package com.qixidi.business.service.dictum;

import com.qixidi.business.domain.bo.dictum.DictumCommentBo;
import com.qixidi.business.domain.vo.dictum.DictumCommentVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

/**
 * @author zi-wei
 * @create 2024/12/17 14:15
 */
public interface DictumCommentService {

    /**
     * 新增评论
     *
     * @param bo 评论信息
     * @return 新增后的评论（含后端生成的 id，供前端落定乐观更新节点）
     */
    DictumCommentVo add(DictumCommentBo bo);

    /**
     * 删除评论（软删，仅本人可删）
     *
     * @param id 评论 id
     */
    void delete(Long id);

    TableDataInfo<DictumCommentVo> commentList(Long id, PageQuery pageQuery);

    /**
     * 获取评论列表（接收字符串 ID，避免前端 JavaScript 精度丢失）
     *
     * @param id 名言 ID（字符串格式）
     * @param pageQuery 分页参数
     * @return 评论列表
     */
    TableDataInfo<DictumCommentVo> commentListStr(String id, PageQuery pageQuery);
}
