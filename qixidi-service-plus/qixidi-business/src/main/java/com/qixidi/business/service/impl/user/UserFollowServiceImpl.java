package com.qixidi.business.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.light.exception.base.BaseException;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.user.UserFollowBo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.domain.enums.CountUserType;
import com.qixidi.business.domain.enums.UserFollowType;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.business.service.user.IUserFollowService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserFollowServiceImpl implements IUserFollowService {

    private final UserFollowMapper baseMapper;
    private final CountUserWebsiteMapper countUserWebsiteMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final NewsUserRecordMapper newsUserRecordMapper;
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
            throw new BaseException("你已经关注了该对象");
        }
        if (bo.getType().equals(UserFollowType.b_user_follow.getCode())) {//  用户关注
            countUserWebsiteMapper.updateAdd(bo.getUid(), CountUserType.FOLLOW_COUNT.getCode());
            countUserWebsiteMapper.updateAdd(bo.getTargetId(), CountUserType.FANS_FOLLOW_COUNT.getCode());
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
        } else if (bo.getType().equals(UserFollowType.LABEL_FOLLOW.getCode())) { //标签关注
            labelInfoMapper.updateAddFollow(bo.getTargetId());
            Long aLong = baseMapper.selectCount(new QueryWrapper<UserFollow>()
                    .eq("target_id", bo.getTargetId()).eq("uid", bo.getUid()).eq("type", UserFollowType.LABEL_FOLLOW.getCode()));
            log.error("关注异常，重复关注，target_id：uid：{}，type：{}", bo.getTargetId(), bo.getUid(), UserFollowType.LABEL_FOLLOW.getCode());
            if (aLong > 0) return false;
        }
        return baseMapper.insert(add) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean cancelFollow(UserFollowBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        if (bo.getType().equals(UserFollowType.b_user_follow.getCode())) { //用户关注取消
            countUserWebsiteMapper.updateDelete(bo.getUid(), CountUserType.FOLLOW_COUNT.getCode());
            countUserWebsiteMapper.updateDelete(bo.getTargetId(), CountUserType.FANS_FOLLOW_COUNT.getCode());
            //发送消息
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    newsUserRecordMapper.delete(new QueryWrapper<NewsUserRecord>()
                            .eq("target_uid", bo.getUid())
                            .eq("uid", bo.getTargetId())
                            .eq("type", NewsType.FOLLOW_NEWS.getCode()));
                    //WebSocket推送消息
                    WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetId());
                }
            });
        } else if (bo.getType().equals(UserFollowType.LABEL_FOLLOW.getCode())) { //标签关注取消
            labelInfoMapper.updateDeleteFollow(bo.getTargetId());
        }

        int delete = baseMapper.delete(new QueryWrapper<UserFollow>()
                .eq("uid", bo.getUid()).eq("target_id", bo.getTargetId()).eq("type", bo.getType()));
        return delete > 0;
    }

    @Override
    public Object followList(String uid, Integer type) {
        if (type.equals(UserFollowType.b_user_follow.getCode())) { //用户关注列表
            return baseMapper.followUserList(uid, type);
        } else if (type.equals(UserFollowType.LABEL_FOLLOW.getCode())) {//标签关注列表
            return baseMapper.followLabelList(uid, type);
        }
        return null;
    }

    @Override
    public Object followRoleList(Integer type) {
        String uid = LoginHelper.getTripartiteUuid();
        if (type.equals(UserFollowType.b_user_follow.getCode())) { //用户关注列表
            return baseMapper.followUserList(uid, type);
        } else if (type.equals(UserFollowType.LABEL_FOLLOW.getCode())) {//标签关注列表
            return baseMapper.followLabelList(uid, type);
        }
        return null;
    }
}
