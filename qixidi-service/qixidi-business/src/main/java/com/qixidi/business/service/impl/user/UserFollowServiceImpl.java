package com.qixidi.business.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.light.exception.ServiceException;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.user.UserFollowBo;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.domain.enums.UserFollowTypeEnums;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.qixidi.business.service.impl.article.ArticleInformationServiceImpl;
import com.qixidi.business.service.user.IUserFollowService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserFollowServiceImpl implements IUserFollowService {

    private final UserFollowMapper baseMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final NewsUserRecordMapper newsUserRecordMapper;
    // recordArticleIntimacy 声明在实现类上（跨 service 沿用 FabulousRecord/Collection 同款注入）
    private final ArticleInformationServiceImpl articleInformationService;
    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertByBo(UserFollowBo bo) {
        bo.setCreateTime(new Date());
        bo.setUid(LoginHelper.getTripartiteUuid());
        UserFollow add = BeanUtil.toBean(bo, UserFollow.class);
        List<UserFollow> userFollows = baseMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUid, bo.getUid())
                .eq(UserFollow::getTargetId, bo.getTargetId())
                .eq(UserFollow::getType, bo.getType())
                .eq(UserFollow::getState, 0));
        if (CollectionUtil.isNotEmpty(userFollows)) {
            throw new ServiceException("你已经关注了该对象");
        }
        if (bo.getType().equals(UserFollowTypeEnums.b_user_follow.getCode())) {//  用户关注
//            发送消息
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    NewsUserRecord newsUserRecord = new NewsUserRecord();
                    newsUserRecord.setUid(bo.getTargetId());
                    newsUserRecord.setTargetUid(bo.getUid());
                    newsUserRecord.setType(NewsType.FOLLOW_NEWS.getCode());
                    newsUserRecord.setCreateTime(new Date());
                    newsUserRecordMapper.insert(newsUserRecord);
                    //WebSocket推送消息
                    WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetId());
                }
            });
        } else if (bo.getType().equals(UserFollowTypeEnums.LABEL_FOLLOW.getCode())) { //标签关注
            labelInfoMapper.updateAddFollow(bo.getTargetId());
            // 记录标签亲密度（关注是强兴趣信号，分值高于浏览/点赞/收藏）
            articleInformationService.recordArticleIntimacy(bo.getUid(), bo.getTargetId(), 5D);
        }
        return baseMapper.insert(add) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelFollow(UserFollowBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        int delete = baseMapper.delete(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUid, bo.getUid())
                .eq(UserFollow::getTargetId, bo.getTargetId())
                .eq(UserFollow::getType, bo.getType()));
        // 真正删到关注记录才回退计数与亲密度（原实现无条件回退，未关注时取消会把计数扣脏）
        if (delete > 0 && bo.getType().equals(UserFollowTypeEnums.LABEL_FOLLOW.getCode())) { //标签关注取消
            labelInfoMapper.updateDeleteFollow(bo.getTargetId());
            articleInformationService.recordArticleIntimacy(bo.getUid(), bo.getTargetId(), -5D);
        }
        return delete > 0;
    }

    @Override
    public Object followList(String uid, Integer type) {
        if (type.equals(UserFollowTypeEnums.b_user_follow.getCode())) { //用户关注列表
            return baseMapper.followUserList(uid, type);
        } else if (type.equals(UserFollowTypeEnums.LABEL_FOLLOW.getCode())) {//标签关注列表
            return followLabelList(uid, type);
        }
        return null;
    }

    @Override
    public Object followRoleList(Integer type) {
        String uid = LoginHelper.getTripartiteUuid();
        if (type.equals(UserFollowTypeEnums.b_user_follow.getCode())) { //用户关注列表
            return baseMapper.followUserList(uid, type);
        } else if (type.equals(UserFollowTypeEnums.LABEL_FOLLOW.getCode())) {//标签关注列表
            return followLabelList(uid, type);
        }
        return null;
    }

    /**
     * 标签关注列表（原单条 SQL join 标签表改为两步单表：查关注记录后批量查标签域组装；
     * LEFT JOIN 语义保留——标签已删的记录仍返回，标签字段为空，仅关注时间有值）
     */
    private List<LabelInfoVo> followLabelList(String uid, Integer type) {
        List<UserFollow> follows = baseMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUid, uid)
                .eq(UserFollow::getType, type)
                .eq(UserFollow::getState, 0));
        if (follows.isEmpty()) {
            return new ArrayList<>();
        }
        List<Long> labelIds = follows.stream()
                .map(UserFollow::getTargetId)
                .filter(StringUtils::isNotBlank)
                .filter(s -> s.matches("\\d+"))
                .distinct()
                .map(Long::valueOf)
                .toList();
        Map<Long, LabelInfo> labels = labelIds.isEmpty() ? Collections.emptyMap()
                : labelInfoMapper.selectBatchIds(labelIds).stream()
                        .collect(Collectors.toMap(LabelInfo::getId, l -> l, (a, b) -> a));
        return follows.stream().map(f -> {
            LabelInfo label = labels.get(NumberUtil.isLong(f.getTargetId()) ? Long.valueOf(f.getTargetId()) : null);
            LabelInfoVo vo = label == null ? new LabelInfoVo() : BeanUtil.toBean(label, LabelInfoVo.class);
            vo.setCreateTime(f.getCreateTime());
            return vo;
        }).toList();
    }
}
