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

    void add(DictumCommentBo bo);

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
