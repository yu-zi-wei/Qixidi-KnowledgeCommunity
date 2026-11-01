package com.qixidi.business.service.impl.news;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.news.NewsSystemInfoBo;
import com.qixidi.business.domain.entity.news.NewsSystemInfo;
import com.qixidi.business.domain.vo.news.NewsSystemInfoVo;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.qixidi.business.service.news.INewsSystemInfoService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.light.webSocket.utils.WebSocketUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统消息Service业务层处理
 *
 * @author aurora
 * @date 2023-04-23
 */
@RequiredArgsConstructor
@Service
public class NewsSystemInfoServiceImpl implements INewsSystemInfoService {

    private final NewsSystemInfoMapper baseMapper;

    /**
     * 查询系统消息
     *
     * @param id 系统消息主键
     * @return 系统消息
     */
    @Override
    public NewsSystemInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询系统消息列表
     *
     * @param bo 系统消息
     * @return 系统消息
     */
    @Override
    public TableDataInfo<NewsSystemInfoVo> queryPageList(NewsSystemInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NewsSystemInfo> lqw = buildQueryWrapper(bo);
        Page<NewsSystemInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询系统消息列表
     *
     * @param bo 系统消息
     * @return 系统消息
     */
    @Override
    public List<NewsSystemInfoVo> queryList(NewsSystemInfoBo bo) {
        LambdaQueryWrapper<NewsSystemInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<NewsSystemInfo> buildQueryWrapper(NewsSystemInfoBo bo) {
        LambdaQueryWrapper<NewsSystemInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getNewsTitle()), NewsSystemInfo::getNewsTitle, bo.getNewsTitle());
        lqw.eq(bo.getIsMassAir() != null, NewsSystemInfo::getIsMassAir, bo.getIsMassAir());
        lqw.eq(bo.getState() != null, NewsSystemInfo::getState, bo.getState());
        lqw.orderByDesc(NewsSystemInfo::getCreateTime);
        return lqw;
    }

    /**
     * 新增系统消息（后台群发）
     * <p>
     * 前台消费链路只认 type=2 + is_mass_air=1（selectUid/selectBaseUid 的过滤条件），
     * 这里统一固化；is_details 门户无消费固定 1。保存即对全体用户可见。
     *
     * @param bo 系统消息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(NewsSystemInfoBo bo) {
        NewsSystemInfo add = BeanUtil.toBean(bo, NewsSystemInfo.class);
        add.setType(2L);
        add.setIsMassAir(1L);
        add.setState(0L);
        add.setUid(null);
        if (add.getIsDetails() == null) {
            add.setIsDetails(1L);
        }
        add.setCreateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            // 给所有在线用户推红点（未读消息数刷新），离线用户下次拉取自然可见
            for (String uid : WebSocketUtils.getAllSession().keySet()) {
                WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(uid);
            }
        }
        return flag;
    }

    /**
     * 失效/恢复系统消息（终审操作：只切 state，不触碰内容）
     *
     * @param id    系统消息主键
     * @param state 目标状态（0：生效，1：失效）
     * @return 结果
     */
    @Override
    public Boolean updateState(Long id, Long state) {
        NewsSystemInfo update = new NewsSystemInfo();
        update.setId(id);
        update.setState(state);
        boolean flag = baseMapper.updateById(update) > 0;
        if (flag) {
            // 状态变化影响全员消息列表，在线用户推送刷新
            for (String uid : WebSocketUtils.getAllSession().keySet()) {
                WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(uid);
            }
        }
        return flag;
    }

    /**
     * 修改系统消息
     *
     * @param bo 系统消息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(NewsSystemInfoBo bo) {
        NewsSystemInfo update = BeanUtil.toBean(bo, NewsSystemInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除系统消息
     *
     * @param ids 需要删除的系统消息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
