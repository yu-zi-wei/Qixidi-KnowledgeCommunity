package com.qixidi.business.service.impl.news;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.light.exception.ServiceException;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.light.webSocket.utils.WebSocketUtils;
import com.qixidi.business.domain.bo.news.NewsSystemInfoBo;
import com.qixidi.business.domain.entity.news.NewsSystemInfo;
import com.qixidi.business.domain.vo.news.NewsSystemInfoVo;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.qixidi.business.service.news.INewsSystemInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;

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
     * 查询系统消息（join 接收人昵称，queryById 禁止单表 selectVoById：展示字段是 join 产物）
     */
    @Override
    public NewsSystemInfoVo queryById(Long id) {
        QueryWrapper<NewsSystemInfo> wrapper = Wrappers.query();
        wrapper.eq("nsi.id", id);
        List<NewsSystemInfoVo> records = baseMapper.selectSystemPage(Page.of(1, 1), wrapper).getRecords();
        return records.isEmpty() ? null : records.get(0);
    }

    /**
     * 查询系统消息列表
     *
     * @param bo 系统消息
     * @return 系统消息
     */
    @Override
    public TableDataInfo<NewsSystemInfoVo> queryPageList(NewsSystemInfoBo bo, PageQuery pageQuery) {
        QueryWrapper<NewsSystemInfo> wrapper = buildQueryWrapper(bo);
        IPage<NewsSystemInfoVo> result = baseMapper.selectSystemPage(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询系统消息列表（导出用，不分页全查：Page.of(1, -1) 跳过分页）
     *
     * @param bo 系统消息
     * @return 系统消息
     */
    @Override
    public List<NewsSystemInfoVo> queryList(NewsSystemInfoBo bo) {
        return baseMapper.selectSystemPage(Page.of(1, -1), buildQueryWrapper(bo)).getRecords();
    }

    /**
     * 列表筛选（join b_user_main 的 SQL，条件列必须带 nsi 别名）
     */
    private QueryWrapper<NewsSystemInfo> buildQueryWrapper(NewsSystemInfoBo bo) {
        QueryWrapper<NewsSystemInfo> wrapper = Wrappers.query();
        wrapper.like(StringUtils.isNotBlank(bo.getNewsTitle()), "nsi.news_title", bo.getNewsTitle());
        wrapper.in(CollectionUtils.isNotEmpty(bo.getIsMassAirs()), "nsi.is_mass_air", bo.getIsMassAirs());
        wrapper.in(CollectionUtils.isNotEmpty(bo.getStates()), "nsi.state", bo.getStates());
        wrapper.orderByDesc("nsi.create_time");
        return wrapper;
    }

    /**
     * 新增系统消息（后台发送）
     * <p>
     * type 统一固化 2（前台消费链路 selectUid/selectBaseUid 只认 type=2）；isMassAir/isDetails 可选，
     * 空时默认群发/无详情；指定人发（isMassAir=2）必须带 uid。保存即对目标用户可见。
     *
     * @param bo 系统消息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(NewsSystemInfoBo bo) {
        NewsSystemInfo add = BeanUtil.toBean(bo, NewsSystemInfo.class);
        if (add.getIsMassAir() == null) {
            add.setIsMassAir(1L);
        }
        checkDesignated(add.getIsMassAir(), bo.getUid());
        if (add.getIsDetails() == null) {
            add.setIsDetails(1L);
        }
        add.setType(2L);
        add.setState(0L);
        if (Long.valueOf(1L).equals(add.getIsMassAir())) {
            add.setUid(null);
        }
        add.setCreateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
            pushNotice(add.getIsMassAir(), add.getUid());
        }
        return flag;
    }

    /**
     * 指定人发必须携带接收人 uid
     */
    private void checkDesignated(Long isMassAir, String uid) {
        if (Long.valueOf(2L).equals(isMassAir) && StringUtils.isBlank(uid)) {
            throw new ServiceException("指定用户发送时必须选择接收用户");
        }
    }

    /**
     * 推送红点：群发推所有在线用户；指定人发只推目标用户（离线用户下次拉取自然可见）
     */
    private void pushNotice(Long isMassAir, String uid) {
        if (Long.valueOf(2L).equals(isMassAir)) {
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(uid);
            return;
        }
        for (String onlineUid : WebSocketUtils.getAllSession().keySet()) {
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(onlineUid);
        }
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
     * 修改系统消息（改发送方式为指定人发时必须携带 uid；null 字段不更新）
     *
     * @param bo 系统消息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(NewsSystemInfoBo bo) {
        checkDesignated(bo.getIsMassAir(), bo.getUid());
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
