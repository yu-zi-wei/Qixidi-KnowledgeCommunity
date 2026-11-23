package com.qixidi.business.service;

import com.light.core.core.domain.vo.CensusVo;
import com.qixidi.business.domain.bo.user.BrowsingHistoryBo;
import com.qixidi.business.domain.vo.user.BrowsingHistoryVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

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

    /**
     * 记录用户足迹（浏览/点赞/评论共用）：未登录静默跳过，同内容同天幂等合并（刷新 update_time 与最近行为类型）。
     * 必须在请求线程调用（依赖 Sa-Token 登录上下文）。
     *
     * @param targetId   目标内容 id（文章 id / 小记 id）
     * @param targetTitle 目标标题
     * @param targetUid  目标作者 uid
     * @param targetType 目标类型（1：文章，2：小记）
     * @param actionType 行为类型（1：阅读，2：点赞，3：评论）
     * @param content    评论内容快照（仅评论行为传；传 null 不覆盖已有快照——「最近」是快照，评论删除不影响）
     */
    void recordFootprint(Long targetId, String targetTitle, String targetUid, Integer targetType, Integer actionType, String content);

    /**
     * 近30天我的内容浏览趋势（按天聚合）
     */
    List<CensusVo> browseTrend();
}

