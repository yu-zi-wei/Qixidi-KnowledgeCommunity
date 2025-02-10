package com.qixidi.business.service.dictum;

import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 名言信息Service接口
 *
 * @author aurora
 * @date 2023-04-24
 */
public interface IDictumInfoService {

    /**
     * 查询名言信息
     *
     * @param id 名言信息主键
     * @return 名言信息
     */
    DictumInfoVo queryById(Long id);

    /**
     * 查询名言信息列表
     *
     * @param bo 名言信息
     * @return 名言信息集合
     */
    TableDataInfo<DictumInfoVo> queryPageList(DictumInfoBo bo, PageQuery pageQuery);

    /**
     * 查询名言信息列表
     *
     * @param bo 名言信息
     * @return 名言信息集合
     */
    List<DictumInfoVo> queryList(DictumInfoBo bo);

    /**
     * 修改名言信息
     *
     * @param bo 名言信息
     * @return 结果
     */
    Boolean insertByBo(DictumInfoBo bo);

    /**
     * 修改名言信息
     *
     * @param bo 名言信息
     * @return 结果
     */
    Boolean updateByBo(DictumInfoBo bo);

    /**
     * 校验并批量删除名言信息信息
     *
     * @param ids     需要删除的名言信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean deleteWithValidById(Long id, Long groupId);
}

