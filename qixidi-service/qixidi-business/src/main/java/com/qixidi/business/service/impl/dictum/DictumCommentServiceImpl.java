package com.qixidi.business.service.impl.dictum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.qixidi.business.domain.bo.dictum.DictumCommentBo;
import com.qixidi.business.domain.entity.dictum.DictumComment;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.dictum.DictumCommentVo;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.dictum.DictumCommentMapper;
import com.qixidi.business.service.dictum.DictumCommentService;
import com.light.core.core.domain.PageQuery;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.common.domain.enums.StatusEnums;
import com.light.exception.ServiceException;
import com.qixidi.auth.helper.LoginHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zi-wei
 * @create 2024/12/17 14:15
 */
@Service
public class DictumCommentServiceImpl implements DictumCommentService {
    @Autowired
    private DictumCommentMapper dictumCommentMapper;
    @Autowired
    private TripartiteUserMapper tripartiteUserMapper;
    @Autowired
    private NewsUserRecordMapper newsUserRecordMapper;

    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;

    @Override
    public DictumCommentVo add(DictumCommentBo bo) {
        DictumComment dictumComment = BeanUtil.copyProperties(bo, DictumComment.class);
        dictumComment.setCommentUid(LoginHelper.getTripartiteUuid());
        dictumComment.setCreateTime(new Date());
        if (bo.getType() == 2) {//二级评论
            Long l = dictumCommentMapper.selectCount(new LambdaQueryWrapper<DictumComment>()
                .eq(DictumComment::getParentId, bo.getParentId())
                .eq(DictumComment::getStatus, StatusEnums.NORMAL.getCode()));
            if (l >= 1000) {//子集评论最大挂载1000条数据
                throw new ServiceException("子级评论已达上限，请新开评论进行回复！");
            }
        }
        int insert = dictumCommentMapper.insert(dictumComment);
        if (insert < 0) {
            throw new ServiceException("添加失败");
        }
        //通知被评论人（一级评论通知随笔作者、回复通知被回复人），自己评论自己不发；异步落库 + WebSocket 推送
        if (!dictumComment.getCommentUid().equals(bo.getTargetUid())) {
            executorService.execute(() -> {
                NewsUserRecord newsUserRecord = new NewsUserRecord();
                newsUserRecord.setUid(bo.getTargetUid());
                newsUserRecord.setNewsId(dictumComment.getId());
                newsUserRecord.setTargetUid(dictumComment.getCommentUid());
                newsUserRecord.setType(NewsType.DICTUM_COMMENT_NEWS.getCode());
                newsUserRecord.setCreateTime(new Date());
                newsUserRecordMapper.insert(newsUserRecord);
                WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
            });
        }
        //插入后主键已回填 entity，直接转 VO 返回（用户昵称头像由前端登录态填充，不再查库）
        return BeanUtil.copyProperties(dictumComment, DictumCommentVo.class);
    }

    @Override
    public void delete(Long id) {
        DictumComment comment = dictumCommentMapper.selectById(id);
        if (comment == null || Objects.equals(comment.getStatus(), StatusEnums.DELETE.getCode())) {
            throw new ServiceException("评论不存在");
        }
        if (!Objects.equals(comment.getCommentUid(), LoginHelper.getTripartiteUuid())) {
            throw new ServiceException("只能删除自己的评论");
        }
        dictumCommentMapper.update(null, new LambdaUpdateWrapper<DictumComment>()
            .eq(DictumComment::getId, id)
            .set(DictumComment::getStatus, StatusEnums.DELETE.getCode()));
        //同步删除该评论产生的通知记录：news_id 存的是评论 id，各业务评论表独立自增会撞车，必须限定 type=随笔评论，防止误删其他业务的通知
        newsUserRecordMapper.delete(new LambdaQueryWrapper<NewsUserRecord>()
            .eq(NewsUserRecord::getType, NewsType.DICTUM_COMMENT_NEWS.getCode())
            .eq(NewsUserRecord::getNewsId, id));
    }

    @Override
    public TableDataInfo<DictumCommentVo> commentList(Long id, PageQuery pageQuery) {
        //一级评论
        IPage<DictumCommentVo> dictumCommentVoIPage = dictumCommentMapper.selectVoPage(pageQuery.build(),
            new LambdaQueryWrapper<DictumComment>().eq(DictumComment::getDictumId, id)
                .eq(DictumComment::getType, 1)
                .eq(DictumComment::getStatus, StatusEnums.NORMAL.getCode())
                .orderByDesc(DictumComment::getCreateTime));
        List<DictumCommentVo> records = dictumCommentVoIPage.getRecords();
        if (CollectionUtil.isEmpty(records)) return new TableDataInfo();

        //二级评论
        List<Long> ids = records.stream().map(DictumCommentVo::getId).collect(Collectors.toList());
        List<DictumCommentVo> dictumCommentLevelIPage = dictumCommentMapper.selectVoList(new LambdaQueryWrapper<DictumComment>()
            .in(DictumComment::getParentId, ids)
            .eq(DictumComment::getStatus, StatusEnums.NORMAL.getCode())
            .orderByAsc(DictumComment::getCreateTime));

        Set<String> uids = new HashSet<>();
        records.forEach(item -> uids.add(item.getCommentUid()));
        if (CollectionUtil.isNotEmpty(dictumCommentLevelIPage)) {
            dictumCommentLevelIPage.forEach(item -> {
                uids.add(item.getCommentUid());
                uids.add(item.getTargetUid());
            });
        }

        List<TripartiteUser> tripartiteUserVos = tripartiteUserMapper.selectList(new LambdaQueryWrapper<TripartiteUser>()
            .in(TripartiteUser::getUuid, uids));
        Map<String, TripartiteUser> userMap = tripartiteUserVos.stream().collect(Collectors.toMap(TripartiteUser::getUuid, item -> item));
        Map<Long, List<DictumCommentVo>> levelmap = new HashMap<>();
        for (DictumCommentVo dictumCommentVo : dictumCommentLevelIPage) {
            List<DictumCommentVo> dictumCommentVos = levelmap.computeIfAbsent(dictumCommentVo.getParentId(), k -> new ArrayList<>());
            //填充用户信息
            fillUserInfo(userMap.get(dictumCommentVo.getCommentUid()), dictumCommentVo, false);
            fillUserInfo(userMap.get(dictumCommentVo.getTargetUid()), dictumCommentVo, true);
            dictumCommentVos.add(dictumCommentVo);
        }
        //组装数据
        for (DictumCommentVo record : records) {
            fillUserInfo(userMap.get(record.getCommentUid()), record, false);
            List<DictumCommentVo> dictumCommentVos = levelmap.get(record.getId());
            if (dictumCommentVos != null) {
                record.setDictumCommentVoList(dictumCommentVos);
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
    private void fillUserInfo(TripartiteUser user, DictumCommentVo vo, boolean isTarget) {
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

    /**
     * 获取评论列表（接收字符串 ID，避免前端 JavaScript 精度丢失）
     *
     * @param id 名言 ID（字符串格式）
     * @param pageQuery 分页参数
     * @return 评论列表
     */
    @Override
    public TableDataInfo<DictumCommentVo> commentListStr(String id, PageQuery pageQuery) {
        // 将字符串转换为 Long，调用原有的 commentList 方法
        return commentList(Long.parseLong(id), pageQuery);
    }
}
