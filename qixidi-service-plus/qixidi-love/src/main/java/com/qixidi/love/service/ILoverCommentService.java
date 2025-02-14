/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:31
 * @version 1.0
 */
package com.qixidi.love.service;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.love.domain.bo.LoverCommentBo;
import com.qixidi.love.domain.vo.LoverCommentVo;

import java.util.Collection;
import java.util.List;

/**
 * 爱情祝福语Service接口
 *
 * @author ziwei
 * @date 2022-11-30
 */
public interface ILoverCommentService {

    /**
     * 查询爱情祝福语
     */
    LoverCommentVo queryById(Long id);

    /**
     * 查询爱情祝福语列表
     */
    TableDataInfo<LoverCommentVo> queryPageList(LoverCommentBo bo, PageQuery pageQuery);

    /**
     * 查询爱情祝福语列表
     */
    List<LoverCommentVo> queryList(LoverCommentBo bo);

    /**
     * 新增爱情祝福语
     */
    Boolean insertByBo(LoverCommentBo bo);

    /**
     * 修改爱情祝福语
     */
    Boolean updateByBo(LoverCommentBo bo);

    /**
     * 校验并批量删除爱情祝福语信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Integer insert(LoverCommentBo bo);
}
