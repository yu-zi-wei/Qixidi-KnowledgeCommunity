package com.qixidi.business.service.dictum;

import com.qixidi.business.domain.bo.dictum.DictumGroupBo;
import com.qixidi.business.domain.vo.dictum.DictumGroupVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 名言分组Service接口
 *
 * @author aurora
 * @date 2023-04-24
 */
public interface IDictumGroupService {

    /**
     * 查询名言分组
     *
     * @param id 名言分组主键
     * @return 名言分组
     */
    DictumGroupVo queryById(Long id);

    /**
     * 查询名言分组列表
     *
     * @param bo 名言分组
     * @return 名言分组集合
     */
    TableDataInfo<DictumGroupVo> queryPageList(DictumGroupBo bo, PageQuery pageQuery);

    /**
     * 查询名言分组列表
     *
     * @param bo 名言分组
     * @return 名言分组集合
     */
    List<DictumGroupVo> queryList(DictumGroupBo bo);

    /**
     * 修改名言分组
     *
     * @param bo 名言分组
     * @return 结果
     */
    Boolean insertByBo(DictumGroupBo bo);

    /**
     * 修改名言分组
     *
     * @param bo 名言分组
     * @return 结果
     */
    Boolean updateByBo(DictumGroupBo bo);

    /**
     * 校验并批量删除名言分组信息
     *
     * @param ids 需要删除的名言分组主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

