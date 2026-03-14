package com.qixidi.business.service.label;

import com.qixidi.business.domain.bo.label.LabelInfoBo;
import com.qixidi.business.domain.vo.label.LabelGroupingInfoVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 标签信息Service接口
 *
 * @author aurora
 * @date 2022-08-16
 */
public interface ILabelInfoService {

    /**
     * 查询标签信息
     *
     * @param id 标签信息主键
     * @return 标签信息
     */
    LabelInfoVo queryById(Long id);

    /**
     * 查询标签信息列表
     *
     * @param pageQuery 标签信息
     * @return 标签信息集合
     */
    TableDataInfo<LabelInfoVo> queryPageList(LabelInfoBo bo, PageQuery pageQuery);

    /**
     * 查询标签信息列表
     *
     * @param bo 标签信息
     * @return 标签信息集合
     */
    List<LabelInfoVo> queryList(LabelInfoBo bo);

    /**
     * 修改标签信息
     *
     * @param bo 标签信息
     * @return 结果
     */
    Boolean insertByBo(LabelInfoBo bo);

    /**
     * 修改标签信息
     *
     * @param bo 标签信息
     * @return 结果
     */
    Boolean updateByBo(LabelInfoBo bo);

    /**
     * 校验并批量删除标签信息信息
     *
     * @param ids 需要删除的标签信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<LabelInfoVo> fdLabelList(LabelInfoBo bo);

    LabelInfoVo fdLabelInfo(Long id,Long type);

    LabelGroupingInfoVo LabelGroupingInfo(Long id);

    List<LabelInfoVo> systemLabel(String label);

}
