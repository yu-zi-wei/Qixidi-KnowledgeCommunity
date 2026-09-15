package com.qixidi.business.service.impl.timeNotes;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.exception.ServiceException;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.timeNotes.TimeNotesCommentBo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.entity.timeNotes.TimeNotesComment;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.timeNotes.TimeNotesCommentVo;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.timeNotes.TimeNotesCommentMapper;
import com.qixidi.business.service.timeNotes.TimeNotesCommentService;
import com.qixidi.common.domain.enums.StatusEnums;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 时光小记评论 Service 实现
 *
 * @author zi-wei
 * @date 2026-09-15
 */
@Service
public class TimeNotesCommentServiceImpl implements TimeNotesCommentService {

    /**
     * 单楼层子评论上限
     */
    private static final long MAX_CHILD_COUNT = 1000;

    @Autowired
    private TimeNotesCommentMapper timeNotesCommentMapper;
    @Autowired
    private TripartiteUserMapper tripartiteUserMapper;
    @Autowired
    private NewsUserRecordMapper newsUserRecordMapper;

    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;

    @Override
    public TimeNotesCommentVo add(TimeNotesCommentBo bo) {
        TimeNotesComment comment = BeanUtil.copyProperties(bo, TimeNotesComment.class);
        comment.setCommentUid(LoginHelper.getTripartiteUuid());
        comment.setCreateTime(new Date());
        if (bo.getType() == 2) {//二级评论
            Long count = timeNotesCommentMapper.selectCount(new LambdaQueryWrapper<TimeNotesComment>()
                .eq(TimeNotesComment::getParentId, bo.getParentId())
                .eq(TimeNotesComment::getStatus, StatusEnums.NORMAL.getCode()));
            if (count >= MAX_CHILD_COUNT) {
                throw new ServiceException("子级评论已达上限，请新开评论进行回复！");
            }
        }
        int insert = timeNotesCommentMapper.insert(comment);
        if (insert < 0) {
            throw new ServiceException("添加失败");
        }
        //通知被评论人（一级评论通知小记作者、回复通知被回复人），自己评论自己不发；异步落库 + WebSocket 推送
        if (!comment.getCommentUid().equals(bo.getTargetUid())) {
            executorService.execute(() -> {
                NewsUserRecord newsUserRecord = new NewsUserRecord();
                newsUserRecord.setUid(bo.getTargetUid());
                newsUserRecord.setNewsId(comment.getId());
                newsUserRecord.setTargetUid(comment.getCommentUid());
                newsUserRecord.setType(NewsType.TIME_NOTES_COMMENT_NEWS.getCode());
                newsUserRecord.setCreateTime(new Date());
                newsUserRecordMapper.insert(newsUserRecord);
                WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
            });
        }
        //插入后主键已回填 entity，直接转 VO 返回（用户昵称头像由前端登录态填充，不再查库）
        return BeanUtil.copyProperties(comment, TimeNotesCommentVo.class);
    }

    @Override
    public void delete(Long id) {
        TimeNotesComment comment = timeNotesCommentMapper.selectById(id);
        if (comment == null || Objects.equals(comment.getStatus(), StatusEnums.DELETE.getCode())) {
            throw new ServiceException("评论不存在");
        }
        if (!Objects.equals(comment.getCommentUid(), LoginHelper.getTripartiteUuid())) {
            throw new ServiceException("只能删除自己的评论");
        }
        timeNotesCommentMapper.update(null, new LambdaUpdateWrapper<TimeNotesComment>()
            .eq(TimeNotesComment::getId, id)
            .set(TimeNotesComment::getStatus, StatusEnums.DELETE.getCode()));
        //同步删除该评论产生的通知记录：news_id 存的是评论 id，各业务评论表独立自增会撞车，必须限定 type=小记评论，防止误删其他业务的通知
        newsUserRecordMapper.delete(new LambdaQueryWrapper<NewsUserRecord>()
            .eq(NewsUserRecord::getType, NewsType.TIME_NOTES_COMMENT_NEWS.getCode())
            .eq(NewsUserRecord::getNewsId, id));
    }

    @Override
    public TableDataInfo<TimeNotesCommentVo> commentList(Long id, PageQuery pageQuery) {
        //一级评论
        IPage<TimeNotesCommentVo> commentPage = timeNotesCommentMapper.selectVoPage(pageQuery.build(),
            new LambdaQueryWrapper<TimeNotesComment>().eq(TimeNotesComment::getTimeNotesId, id)
                .eq(TimeNotesComment::getType, 1)
                .eq(TimeNotesComment::getStatus, StatusEnums.NORMAL.getCode())
                .orderByDesc(TimeNotesComment::getCreateTime));
        List<TimeNotesCommentVo> records = commentPage.getRecords();
        if (CollectionUtil.isEmpty(records)) return new TableDataInfo();

        //二级评论
        List<Long> ids = records.stream().map(TimeNotesCommentVo::getId).collect(Collectors.toList());
        List<TimeNotesCommentVo> childComments = timeNotesCommentMapper.selectVoList(new LambdaQueryWrapper<TimeNotesComment>()
            .in(TimeNotesComment::getParentId, ids)
            .eq(TimeNotesComment::getStatus, StatusEnums.NORMAL.getCode())
            .orderByAsc(TimeNotesComment::getCreateTime));

        Set<String> uids = new HashSet<>();
        records.forEach(item -> uids.add(item.getCommentUid()));
        if (CollectionUtil.isNotEmpty(childComments)) {
            childComments.forEach(item -> {
                uids.add(item.getCommentUid());
                uids.add(item.getTargetUid());
            });
        }

        List<TripartiteUser> users = tripartiteUserMapper.selectList(new LambdaQueryWrapper<TripartiteUser>()
            .in(TripartiteUser::getUuid, uids));
        Map<String, TripartiteUser> userMap = users.stream().collect(Collectors.toMap(TripartiteUser::getUuid, item -> item));
        Map<Long, List<TimeNotesCommentVo>> childMap = new HashMap<>();
        for (TimeNotesCommentVo child : childComments) {
            List<TimeNotesCommentVo> children = childMap.computeIfAbsent(child.getParentId(), k -> new ArrayList<>());
            //填充用户信息
            fillUserInfo(userMap.get(child.getCommentUid()), child, false);
            fillUserInfo(userMap.get(child.getTargetUid()), child, true);
            children.add(child);
        }
        //组装数据
        for (TimeNotesCommentVo record : records) {
            fillUserInfo(userMap.get(record.getCommentUid()), record, false);
            List<TimeNotesCommentVo> children = childMap.get(record.getId());
            if (children != null) {
                record.setChildren(children);
            }
        }
        return TableDataInfo.build(records);
    }

    /**
     * 填充评论用户信息（用户可能已注销，需判空）
     *
     * @param user     用户信息
     * @param vo       评论 VO
     * @param isTarget true 填充目标用户字段，false 填充评论人字段
     */
    private void fillUserInfo(TripartiteUser user, TimeNotesCommentVo vo, boolean isTarget) {
        if (user == null) {
            return;
        }
        if (isTarget) {
            vo.setTargetUsername(user.getUsername());
            vo.setTargetNickname(user.getNickname());
            vo.setTargetAvatar(user.getAvatar());
        } else {
            vo.setUsername(user.getUsername());
            vo.setNickname(user.getNickname());
            vo.setAvatar(user.getAvatar());
        }
    }
}
