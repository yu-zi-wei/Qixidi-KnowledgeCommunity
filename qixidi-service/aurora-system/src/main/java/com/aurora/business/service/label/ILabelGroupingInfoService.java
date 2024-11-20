package com.aurora.business.service.label;

import com.aurora.business.domain.bo.label.LabelGroupingInfoBo;
import com.aurora.business.domain.vo.label.LabelGroupingInfoVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 标签分组信息Service接口
 *
 * @author aurora
 * @date 2022-08-16
 */
public interface ILabelGroupingInfoService {

    /**
     * 查询标签分组信息
     *
     * @param id 标签分组信息主键
     * @return 标签分组信息
     */
    LabelGroupingInfoVo queryById(Long id);

    /**
     * 查询标签分组信息列表
     *
     * @param pageQuery 标签分组信息
     * @return 标签分组信息集合
     */
    TableDataInfo<LabelGroupingInfoVo> queryPageList(LabelGroupingInfoBo bo, PageQuery pageQuery);

    /**
     * 查询标签分组信息列表
     *
     * @param bo 标签分组信息
     * @return 标签分组信息集合
     */
    List<LabelGroupingInfoVo> queryList(LabelGroupingInfoBo bo);

    /**
     * 修改标签分组信息
     *
     * @param bo 标签分组信息
     * @return 结果
     */
    Boolean insertByBo(LabelGroupingInfoBo bo);

    /**
     * 修改标签分组信息
     *
     * @param bo 标签分组信息
     * @return 结果
     */
    Boolean updateByBo(LabelGroupingInfoBo bo);

    /**
     * 校验并批量删除标签分组信息信息
     *
     * @param ids 需要删除的标签分组信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<LabelGroupingInfoVo> fdkGroupingList(LabelGroupingInfoBo bo, PageQuery pageQuery);

}
