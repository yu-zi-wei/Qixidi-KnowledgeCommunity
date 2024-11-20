package com.aurora.business.service;

import com.aurora.business.domain.bo.user.BrowsingHistoryBo;
import com.aurora.business.domain.vo.user.BrowsingHistoryVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户浏览历史

 Service接口
 *
 * @author aurora
 * @date 2023-04-24
 */
public interface IBrowsingHistoryService {

    /**
     * 查询用户浏览历史
     *
     * @param id 用户浏览历史
    主键
     * @return 用户浏览历史

     */
    BrowsingHistoryVo queryById(Long id);

    /**
     * 查询用户浏览历史

     列表
     *
     * @param bo 用户浏览历史


     * @return 用户浏览历史

    集合
     */
    TableDataInfo<BrowsingHistoryVo> queryPageList(BrowsingHistoryBo bo, PageQuery pageQuery);

    /**
     * 查询用户浏览历史

     列表
     *
     * @param bo 用户浏览历史


     * @return 用户浏览历史

    集合
     */
    List<BrowsingHistoryVo> queryList(BrowsingHistoryBo bo);

    /**
     * 修改用户浏览历史
     *
     * @param bo 用户浏览历史


     * @return 结果
     */
    Boolean insertByBo(BrowsingHistoryBo bo);

    /**
     * 修改用户浏览历史
     *
     * @param bo 用户浏览历史


     * @return 结果
     */
    Boolean updateByBo(BrowsingHistoryBo bo);

    /**
     * 校验并批量删除用户浏览历史

     信息
     *
     * @param ids 需要删除的用户浏览历史

    主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<BrowsingHistoryVo> queryPageUidList(BrowsingHistoryBo bo, PageQuery pageQuery);
}

