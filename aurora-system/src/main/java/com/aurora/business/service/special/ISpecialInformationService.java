package com.aurora.business.service.special;

import com.aurora.business.domain.bo.special.SpecialInformationBo;
import com.aurora.business.domain.vo.special.SpecialInformationVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 专栏信息Service接口
 *
 * @author aurora
 * @date 2022-08-19
 */
public interface ISpecialInformationService {

    /**
     * 查询专栏信息
     *
     * @param id 专栏信息主键
     * @return 专栏信息
     */
    SpecialInformationVo queryById(Long id);

    /**
     * 查询专栏信息列表
     *
     * @param pageQuery 专栏信息
     * @return 专栏信息集合
     */
    TableDataInfo<SpecialInformationVo> queryPageList(SpecialInformationBo bo, PageQuery pageQuery);

    /**
     * 查询专栏信息列表
     *
     * @param bo 专栏信息
     * @return 专栏信息集合
     */
    List<SpecialInformationVo> queryList(SpecialInformationBo bo);

    /**
     * 修改专栏信息
     *
     * @param bo 专栏信息
     * @return 结果
     */
    Boolean insertByBo(SpecialInformationBo bo);

    /**
     * 修改专栏信息
     *
     * @param bo 专栏信息
     * @return 结果
     */
    Boolean updateByBo(SpecialInformationBo bo);

    /**
     * 校验并批量删除专栏信息信息
     *
     * @param ids     需要删除的专栏信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

