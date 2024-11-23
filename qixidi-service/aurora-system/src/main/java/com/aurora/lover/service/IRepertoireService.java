/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月22日 16:00
 * @version 1.0
 */
package com.aurora.lover.service;


import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.bo.RepertoireBo;
import com.aurora.lover.domain.vo.RepertoireVo;

import java.util.Collection;
import java.util.List;


/**
 * 爱情清单Service接口
 *
 * @author ziwei
 * @date 2022-11-22
 */
public interface IRepertoireService {

    /**
     * 查询爱情清单
     */
    RepertoireVo queryById(Long id);

    /**
     * 查询爱情清单列表
     */
    TableDataInfo<RepertoireVo> queryPageList(RepertoireBo bo, PageQuery pageQuery);

    /**
     * 查询爱情清单列表
     */
    List<RepertoireVo> queryList(RepertoireBo bo);

    /**
     * 新增爱情清单
     */
    Boolean insertByBo(RepertoireBo bo);

    /**
     * 修改爱情清单
     */
    Boolean updateByBo(RepertoireBo bo);

    /**
     * 校验并批量删除爱情清单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<RepertoireVo> repertoireApi(RepertoireBo bo, PageQuery pageQuery);
}
