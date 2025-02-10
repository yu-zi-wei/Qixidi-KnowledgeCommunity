package com.qixidi.business.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.qixidi.business.domain.bo.user.BrowsingHistoryBo;
import com.qixidi.business.domain.entity.user.BrowsingHistory;
import com.qixidi.business.domain.vo.user.BrowsingHistoryVo;
import com.qixidi.business.mapper.user.BrowsingHistoryMapper;
import com.qixidi.business.service.IBrowsingHistoryService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.helper.LoginHelper;
import com.light.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
}

