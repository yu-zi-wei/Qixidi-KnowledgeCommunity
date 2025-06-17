package com.qixidi.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.qixidi.business.domain.bo.FeedbackBo;
import com.qixidi.business.domain.entity.Feedback;
import com.qixidi.business.domain.entity.news.NewsSystemInfo;
import com.qixidi.business.domain.vo.FeedbackStatusSumVo;
import com.qixidi.business.domain.vo.FeedbackVo;
import com.qixidi.business.mapper.FeedbackMapper;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.business.service.IFeedbackService;
import com.light.core.core.domain.PageQuery;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.enums.FeedbackStatusEnums;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.qixidi.auth.helper.LoginHelper;
import com.light.core.utils.StringUtils;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户反馈Service业务层处理
 *
 * @author aurora
 * @date 2023-04-17
 */
@RequiredArgsConstructor
@Service
public class FeedbackServiceImpl implements IFeedbackService {

    private final FeedbackMapper baseMapper;

    private final TripartiteUserMapper tripartiteUserMapper;
    private final NewsSystemInfoMapper newsSystemInfoMapper;

    /**
     * 查询用户反馈
     *
     * @param id 用户反馈主键
     * @return 用户反馈
     */
    @Override
    public FeedbackVo queryById(Long id) {
        FeedbackVo FeedbackVo = baseMapper.selectVoById(id);
        TripartiteUser tripartiteUser = tripartiteUserMapper.selectOne(new LambdaQueryWrapper<TripartiteUser>()
                .eq(TripartiteUser::getUuid, FeedbackVo.getUpdateBy()));
        FeedbackVo.setUpdateName(tripartiteUser.getNickname());
        return FeedbackVo;
    }

    /**
     * 查询用户反馈列表
     *
     * @param bo 用户反馈
     * @return 用户反馈
     */
    @Override
    public TableDataInfo<FeedbackVo> queryPageList(FeedbackBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Feedback> lqw = buildQueryWrapper(bo);
        Page<FeedbackVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户反馈列表
     *
     * @param bo 用户反馈
     * @return 用户反馈
     */
    @Override
    public List<FeedbackVo> queryList(FeedbackBo bo) {
        LambdaQueryWrapper<Feedback> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Feedback> buildQueryWrapper(FeedbackBo bo) {
        LambdaQueryWrapper<Feedback> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), Feedback::getUid, bo.getUid());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), Feedback::getNickname, bo.getNickname());
        lqw.eq(StringUtils.isNotBlank(bo.getFeedbackContent()), Feedback::getFeedbackContent, bo.getFeedbackContent());
        lqw.eq(bo.getStatus() != null && bo.getStatus() != -1, Feedback::getStatus, bo.getStatus());
        lqw.orderByDesc(Feedback::getCreateTime);
        return lqw;
    }

    /**
     * 新增用户反馈
     *
     * @param bo 用户反馈
     * @return 结果
     */
    @Override
    public Boolean insertByBo(FeedbackBo bo) {
        Feedback add = BeanUtil.toBean(bo, Feedback.class);
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        add.setUid(tripartiteUser.getUuid());
        add.setNickname(tripartiteUser.getNickname());
        add.setUpdateBy(tripartiteUser.getUuid());
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户反馈
     *
     * @param bo 用户反馈
     * @return 结果
     */
    @Override
    public Boolean updateByBo(FeedbackBo bo) {
        Feedback update = BeanUtil.toBean(bo, Feedback.class);
        update.setUpdateBy(LoginHelper.getTripartiteUuid());
        update.setCreateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除用户反馈
     *
     * @param ids 需要删除的用户反馈主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        int update = baseMapper.update(null, new LambdaUpdateWrapper<Feedback>().eq(Feedback::getId, id).set(Feedback::getStatus, status));
        boolean isSucceed = update > 0;
        if (isSucceed) {
            TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
            FeedbackVo FeedbackVo = baseMapper.selectVoById(id);
            //发送系统消息
            String content = String.format("%s 修改了任务：#%s 状态为-[%s]", tripartiteUser.getNickname(), FeedbackVo.getFeedbackTitle(), FeedbackStatusEnums.acquireStatusMessage(status));
            NewsSystemInfo newsSystemInfo = new NewsSystemInfo()
                    .setNewsTitle(content)
                    .setNewsContent(content)
                    .setIsDetails(1L)
                    .setType(2L)
                    .setIsMassAir(2L)
                    .setUid(FeedbackVo.getUid())
                    .setCreateTime(new Date());
            newsSystemInfoMapper.insert(newsSystemInfo);
            //WebSocket推送消息
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(FeedbackVo.getUid());
        }
        return isSucceed;
    }

    @Override
    public FeedbackStatusSumVo statusSum() {
        FeedbackStatusSumVo FeedbackStatusSumVo = new FeedbackStatusSumVo();
        List<Feedback> Feedbacks = baseMapper.selectList(new LambdaQueryWrapper<Feedback>().select(Feedback::getStatus, Feedback::getId));
        if (CollectionUtil.isEmpty(Feedbacks)) return FeedbackStatusSumVo;
        Map<Integer, Long> collect = Feedbacks.stream().collect(Collectors.groupingBy(Feedback::getStatus, Collectors.counting()));
        collect.forEach((k, v) -> {
            if (k.equals(FeedbackStatusEnums.TO_BE_PROCESSED.getCode())) {
                FeedbackStatusSumVo.setToBeProcessed(v);
            } else if (k.equals(FeedbackStatusEnums.UNDER_WAY.getCode())) {
                FeedbackStatusSumVo.setUnderWay(v);
            } else if (k.equals(FeedbackStatusEnums.COMPLETED.getCode())) {
                FeedbackStatusSumVo.setCompleted(v);
            } else if (k.equals(FeedbackStatusEnums.CLOSED.getCode())) {
                FeedbackStatusSumVo.setClosed(v);
            }
        });
        FeedbackStatusSumVo.setAllData(Long.valueOf(Feedbacks.size()));
        return FeedbackStatusSumVo;
    }
}

