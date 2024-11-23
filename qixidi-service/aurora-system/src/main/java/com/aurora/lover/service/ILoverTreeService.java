/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:59
 * @version 1.0
 */
package com.aurora.lover.service;

import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.bo.LoverTreeBo;
import com.aurora.lover.domain.vo.LoverTreeVo;

import java.util.Collection;
import java.util.List;

/**
 * 爱情树Service接口
 *
 * @author ziwei
 * @date 2022-11-30
 */
public interface ILoverTreeService {

    /**
     * 查询爱情树
     */
    LoverTreeVo queryById(Long id);

    /**
     * 查询爱情树列表
     */
    TableDataInfo<LoverTreeVo> queryPageList(LoverTreeBo bo, PageQuery pageQuery);

    /**
     * 查询爱情树列表
     */
    List<LoverTreeVo> queryList(LoverTreeBo bo);

    /**
     * 新增爱情树
     */
    Boolean insertByBo(LoverTreeBo bo);

    /**
     * 修改爱情树
     */
    Boolean updateByBo(LoverTreeBo bo);

    /**
     * 校验并批量删除爱情树信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    LoverTreeVo loverTreeApi();

}
