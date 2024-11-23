/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 16:56
 * @version 1.0
 */
package com.aurora.lover.service;


import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.bo.LoverRecordBo;
import com.aurora.lover.domain.vo.LoverRecordVo;

import java.util.Collection;
import java.util.List;

/**
 * 爱情记录Service接口
 *
 * @author ziwei
 * @date 2022-11-21
 */
public interface ILoverRecordService {

    /**
     * 查询爱情记录
     */
    LoverRecordVo queryById(Long id);

    /**
     * 查询爱情记录列表
     */
    TableDataInfo<LoverRecordVo> queryPageList(LoverRecordBo bo, PageQuery pageQuery);

    /**
     * 查询爱情记录列表
     */
    List<LoverRecordVo> queryList(LoverRecordBo bo);

    /**
     * 新增爱情记录
     */
    Boolean insertByBo(LoverRecordBo bo);

    /**
     * 修改爱情记录
     */
    Boolean updateByBo(LoverRecordBo bo);

    /**
     * 校验并批量删除爱情记录信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<LoverRecordVo> loverRecordApi(LoverRecordBo bo, PageQuery pageQuery);
}
