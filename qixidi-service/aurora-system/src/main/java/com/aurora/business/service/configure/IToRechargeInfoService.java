package com.aurora.business.service.configure;

import com.aurora.business.domain.bo.configure.ToRechargeInfoBo;
import com.aurora.business.domain.vo.configure.ToRechargeInfoVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 充值信息Service接口
 *
 * @author aurora
 * @date 2023-04-04
 */
public interface IToRechargeInfoService {

    /**
     * 查询充值信息
     *
     * @param id 充值信息主键
     * @return 充值信息
     */
    ToRechargeInfoVo queryById(Long id);

    /**
     * 查询充值信息列表
     *
     * @param bo 充值信息
     * @return 充值信息集合
     */
    TableDataInfo<ToRechargeInfoVo> queryPageList(ToRechargeInfoBo bo, PageQuery pageQuery);

    /**
     * 查询充值信息列表
     *
     * @param bo 充值信息
     * @return 充值信息集合
     */
    List<ToRechargeInfoVo> queryList(ToRechargeInfoBo bo);

    /**
     * 修改充值信息
     *
     * @param bo 充值信息
     * @return 结果
     */
    Boolean insertByBo(ToRechargeInfoBo bo);

    /**
     * 修改充值信息
     *
     * @param bo 充值信息
     * @return 结果
     */
    Boolean updateByBo(ToRechargeInfoBo bo);

    /**
     * 校验并批量删除充值信息信息
     *
     * @param ids 需要删除的充值信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<ToRechargeInfoVo> list(ToRechargeInfoBo bo);

}

