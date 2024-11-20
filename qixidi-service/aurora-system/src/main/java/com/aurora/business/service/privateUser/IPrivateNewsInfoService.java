package com.aurora.business.service.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateNewsInfoBo;
import com.aurora.business.domain.vo.privateUser.PrivateNewsInfoVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 私信记录Service接口
 *
 * @author aurora
 * @date 2023-03-23
 */
public interface IPrivateNewsInfoService {

    /**
     * 查询私信记录
     *
     * @param id 私信记录主键
     * @return 私信记录
     */
    PrivateNewsInfoVo queryById(Long id);

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录集合
     */
    TableDataInfo<PrivateNewsInfoVo> queryPageList(PrivateNewsInfoBo bo, PageQuery pageQuery);

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录集合
     */
    List<PrivateNewsInfoVo> queryList(PrivateNewsInfoBo bo);

    /**
     * 修改私信记录
     *
     * @param bo 私信记录
     * @return 结果
     */
    Boolean insertByBo(PrivateNewsInfoBo bo);

    /**
     * 修改私信记录
     *
     * @param bo 私信记录
     * @return 结果
     */
    Boolean updateByBo(PrivateNewsInfoBo bo);

    /**
     * 校验并批量删除私信记录信息
     *
     * @param ids     需要删除的私信记录主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void beenRead(String targetUid);
}

