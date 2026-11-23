package com.qixidi.business.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.qixidi.business.domain.bo.user.BrowsingHistoryBo;
import com.qixidi.business.domain.entity.user.BrowsingHistory;
import com.qixidi.business.domain.vo.user.BrowsingHistoryVo;
import com.qixidi.business.mapper.user.BrowsingHistoryMapper;
import com.qixidi.business.service.IBrowsingHistoryService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.vo.CensusVo;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.helper.LoginHelper;
import com.light.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 用户浏览历史
 * <p>
 * Service业务层处理
 *
 * @author aurora
 * @date 2023-04-24
 */
@RequiredArgsConstructor
@Service
public class BrowsingHistoryServiceImpl implements IBrowsingHistoryService {

    private final BrowsingHistoryMapper baseMapper;

    /**
     * 查询用户浏览历史
     *
     * @param id 用户浏览历史
     *           <p>
     *           主键
     * @return 用户浏览历史
     */
    @Override
    public BrowsingHistoryVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户浏览历史
     * <p>
     * 列表
     *
     * @param bo 用户浏览历史
     * @return 用户浏览历史
     */
    @Override
    public TableDataInfo<BrowsingHistoryVo> queryPageList(BrowsingHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BrowsingHistory> lqw = buildQueryWrapper(bo);
        Page<BrowsingHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户浏览历史
     * <p>
     * 列表
     *
     * @param bo 用户浏览历史
     * @return 用户浏览历史
     */
    @Override
    public List<BrowsingHistoryVo> queryList(BrowsingHistoryBo bo) {
        LambdaQueryWrapper<BrowsingHistory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BrowsingHistory> buildQueryWrapper(BrowsingHistoryBo bo) {
        LambdaQueryWrapper<BrowsingHistory> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), BrowsingHistory::getUid, bo.getUid());
        lqw.eq(bo.getTargetId() != null, BrowsingHistory::getTargetId, bo.getTargetId());
        lqw.eq(StringUtils.isNotBlank(bo.getTargetTitle()), BrowsingHistory::getTargetTitle, bo.getTargetTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getTargetUid()), BrowsingHistory::getTargetUid, bo.getTargetUid());
        lqw.eq(bo.getTargetType() != null, BrowsingHistory::getTargetType, bo.getTargetType());
        lqw.orderByDesc(BrowsingHistory::getUpdateTime);
        return lqw;
    }

    /**
     * 新增用户浏览历史
     *
     * @param bo 用户浏览历史
     * @return 结果
     */
    @Override
    public Boolean insertByBo(BrowsingHistoryBo bo) {
        BrowsingHistory add = BeanUtil.toBean(bo, BrowsingHistory.class);
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return true;
        add.setUid(uuid);
        add.setActionType(bo.getActionType() != null ? bo.getActionType() : 1);
        String today = DateUtil.today();
        add.setCreateTime(DateUtil.parse(today, "yyyy-MM-dd"));
        add.setUpdateTime(new Date());
        boolean flag = baseMapper.insertOrUpdates(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户浏览历史
     *
     * @param bo 用户浏览历史
     * @return 结果
     */
    @Override
    public Boolean updateByBo(BrowsingHistoryBo bo) {
        BrowsingHistory update = BeanUtil.toBean(bo, BrowsingHistory.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除用户浏览历史
     *
     * @param ids 需要删除的用户浏览历史
     *            <p>
     *            主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<BrowsingHistoryVo> queryPageUidList(BrowsingHistoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BrowsingHistory> lqw = buildQueryWrapper(bo);
        Page<BrowsingHistoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 近30天我的内容浏览趋势（按天聚合，同一访问者对同一内容一天内去重）
     */
    @Override
    public List<CensusVo> browseTrend() {
        String uuid = LoginHelper.getTripartiteUuid();
        String time = DateUtil.formatDate(DateUtil.offsetDay(new Date(), -30));
        return baseMapper.selectBrowseTrend(uuid, time);
    }

    /**
     * 记录用户足迹（浏览/点赞/评论共用）：未登录静默跳过。
     * 合并策略：近 30 天内已有同目标足迹则不新增记录，只把最近一条的时间刷新为当前
     * （行为类型归本次；标题刷新为最新；评论快照仅评论行为覆盖，阅读/点赞不清除）。
     * 窗口外插入新行（同天唯一键 + ON DUPLICATE 幂等兜底）。
     * 必须在请求线程调用（依赖 Sa-Token 登录上下文）。
     */
    @Override
    public void recordFootprint(Long targetId, String targetTitle, String targetUid, Integer targetType, Integer actionType, String content) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null || targetId == null) {
            return;
        }
        Date now = new Date();
        // 近 30 天内同目标的最近一条足迹
        BrowsingHistory recent = baseMapper.selectOne(new LambdaQueryWrapper<BrowsingHistory>()
                .eq(BrowsingHistory::getUid, uuid)
                .eq(BrowsingHistory::getTargetId, targetId)
                .eq(BrowsingHistory::getTargetType, targetType)
                .ge(BrowsingHistory::getUpdateTime, DateUtil.offsetDay(now, -30))
                .orderByDesc(BrowsingHistory::getUpdateTime)
                .last("LIMIT 1"));
        if (recent != null) {
            baseMapper.update(null, new LambdaUpdateWrapper<BrowsingHistory>()
                    .eq(BrowsingHistory::getId, recent.getId())
                    .set(BrowsingHistory::getUpdateTime, now)
                    .set(StringUtils.isNotBlank(targetTitle), BrowsingHistory::getTargetTitle, targetTitle)
                    .set(BrowsingHistory::getActionType, actionType != null ? actionType : 1)
                    .set(content != null, BrowsingHistory::getContent,
                            content != null && content.length() > 500 ? content.substring(0, 500) : content));
            return;
        }
        // 窗口外：插入新行
        BrowsingHistory add = new BrowsingHistory();
        add.setUid(uuid);
        add.setTargetId(targetId);
        add.setTargetTitle(targetTitle);
        add.setTargetUid(targetUid);
        add.setTargetType(targetType);
        add.setActionType(actionType != null ? actionType : 1);
        // 评论内容快照截断 500（列宽约束）
        add.setContent(content != null && content.length() > 500 ? content.substring(0, 500) : content);
        // create_time 规整为当天零点：与唯一键配合实现「同内容同天合并」
        add.setCreateTime(DateUtil.parse(DateUtil.today(), "yyyy-MM-dd"));
        add.setUpdateTime(now);
        baseMapper.insertOrUpdates(add);
    }
}

