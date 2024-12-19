package com.aurora.business.service.dictum;

import com.aurora.business.domain.bo.dictum.DictumCommentBo;
import com.aurora.business.domain.vo.dictum.DictumCommentVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

/**
 * @author zi-wei
 * @create 2024/12/17 14:15
 */
public interface DictumCommentService {

    void add(DictumCommentBo bo);

    void delete(Long id);

    TableDataInfo<DictumCommentVo> commentList(Long id, PageQuery pageQuery);
}
