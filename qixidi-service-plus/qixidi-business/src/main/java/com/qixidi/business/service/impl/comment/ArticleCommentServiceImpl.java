package com.qixidi.business.service.impl.comment;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.MsgEnums;
import com.light.core.utils.StringUtils;
import com.light.core.utils.email.MailUtils;
import com.light.core.utils.word.WordFilter;
import com.light.redission.utils.RedisUtils;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.comment.ArticleCommentBo;
import com.qixidi.business.domain.entity.comment.ArticleComment;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.enums.CommentTypeEnums;
import com.qixidi.business.domain.enums.CountUserTypeEnums;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.common.domain.enums.StatusEnums;
import com.qixidi.business.domain.enums.article.ArticleUpdateTypeEnums;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.comment.ArticleCommentVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.comment.ArticleCommentMapper;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.mapper.shield.ToShieldWordMapper;
import com.qixidi.business.service.comment.IArticleCommentService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 文章评论Service业务层处理
 *
 * @author aurora
 * @date 2022-11-03
 */
@RequiredArgsConstructor
@Service
public class ArticleCommentServiceImpl implements IArticleCommentService {

    private final ArticleCommentMapper baseMapper;
    private final CountUserWebsiteMapper countUserWebsiteMapper;
    private final ArticleInformationMapper articleInformationMapper;
    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;
    private final ToShieldWordMapper toShieldWordMapper;
    private final NewsUserRecordMapper newsUserRecordMapper;
    @Autowired
    private TripartiteUserMapper tripartiteUserMapper;

    /**
     * 查询文章评论
     *
     * @param id 文章评论主键
     * @return 文章评论
     */
    @Override
    public ArticleCommentVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询文章评论列表
     *
     * @param bo 文章评论
     * @return 文章评论
     */
    @Override
    public TableDataInfo<ArticleCommentVo> queryPageList(ArticleCommentBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ArticleComment> lqw = buildQueryWrapper(bo);
        Page<ArticleCommentVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询文章评论列表
     *
     * @param bo 文章评论
     * @return 文章评论
     */
    @Override
    public List<ArticleCommentVo> queryList(ArticleCommentBo bo) {
        LambdaQueryWrapper<ArticleComment> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ArticleComment> buildQueryWrapper(ArticleCommentBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ArticleComment> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getArticleId() != null, ArticleComment::getArticleId, bo.getArticleId());
        lqw.eq(bo.getParentId() != null, ArticleComment::getParentId, bo.getParentId());
        lqw.eq(bo.getCommentGrade() != null, ArticleComment::getCommentGrade, bo.getCommentGrade());
        lqw.eq(bo.getTargetId() != null, ArticleComment::getTargetId, bo.getTargetId());
        lqw.eq(StringUtils.isNotBlank(bo.getCommentUid()), ArticleComment::getCommentUid, bo.getCommentUid());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), ArticleComment::getContent, bo.getContent());
        lqw.eq(bo.getType() != null, ArticleComment::getType, bo.getType());
        lqw.eq(bo.getState() != null, ArticleComment::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增文章评论
     *
     * @param bo 文章评论
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean insertByBo(ArticleCommentBo bo) throws Exception {
        bo.setCommentUid(LoginHelper.getTripartiteUuid());
        ArticleComment add = BeanUtil.toBean(bo, ArticleComment.class);
        add.setCreateTime(new Date()).setUpdateTime(new Date());
        //评论审核
        List<String> cacheList = RedisUtils.getCacheList(RedisBusinessKeyEnums.BLOCKING_WORDS.getKey());
        if (CollectionUtils.isEmpty(cacheList)) {
            //        存入缓存
            List<String> stringList = toShieldWordMapper.selectKeyword();
            RedisUtils.setCacheList(RedisBusinessKeyEnums.BLOCKING_WORDS.getKey(), stringList);
            cacheList = stringList;
        }
        WordFilter wordFilter = new WordFilter(cacheList);
        int wordCount = wordFilter.wordCount(bo.getContent());
        if (wordCount > 1) {
            throw new Exception("检测到敏感词，操作失败");
        }

//        前置处理
        commentPreprocessing(bo);
        if (bo.getType().equals(CommentTypeEnums.COMMENT_TYPE.getCode())) {
            countUserWebsiteMapper.updateAdd(bo.getTargetUid(), CountUserTypeEnums.FANS_COMMENT_COUNT.getCode());
        }
        boolean flag = baseMapper.insert(add) > 0;
        if (!bo.getCommentUid().equals(bo.getTargetUid())) {
            //        发送消息
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    NewsUserRecord newsUserRecord = new NewsUserRecord();
                    newsUserRecord.setUid(bo.getTargetUid());
                    newsUserRecord.setNewsId(add.getId());
                    newsUserRecord.setTargetUid(bo.getCommentUid());
                    newsUserRecord.setType(NewsType.COMMENT_NEWS.getCode());
                    newsUserRecord.setCreateTime(new Date());
                    newsUserRecordMapper.insert(newsUserRecord);
                    //WebSocket推送消息
                    WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
                    //发送邮件通知
                    TripartiteUserVo basicsUser = tripartiteUserMapper.getBasicsUser(bo.getTargetUid());
                    if (StrUtil.isNotEmpty(basicsUser.getEmail())) {
                        MailUtils.sendText(basicsUser.getEmail(), "栖息地-收到新评论", add.getContent());
                    }
                }
            });
        }
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    @Async
    public void commentPreprocessing(ArticleCommentBo bo) {
        //        修改评论人评论次数
        countUserWebsiteMapper.updateAdd(bo.getCommentUid(), CountUserTypeEnums.COMMENT_COUNT.getCode());
        //        修改作品作者被评论数
        countUserWebsiteMapper.updateAdd(bo.getUid(), CountUserTypeEnums.FANS_COMMENT_COUNT.getCode());
        //修改文章评论数
        articleInformationMapper.updateAdd(bo.getArticleId(), ArticleUpdateTypeEnums.COMMENT_COUNT.getCode());
        //WebSocket推送消息
        WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
    }


    /**
     * 修改文章评论
     *
     * @param bo 文章评论
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ArticleCommentBo bo) {
        ArticleComment update = BeanUtil.toBean(bo, ArticleComment.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除文章评论
     *
     * @param ids 需要删除的文章评论主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<ArticleCommentVo> ArticleList(ArticleCommentBo bo, PageQuery pageQuery) {
        //一级评论
        List<ArticleCommentVo> list = baseMapper.selectVoList(new LambdaQueryWrapper<ArticleComment>()
                .eq(ArticleComment::getArticleId, bo.getArticleId())
                .eq(ArticleComment::getType, 1)
                .eq(ArticleComment::getState, StatusEnums.NORMAL.getCode())
                .orderByDesc(ArticleComment::getCreateTime));
        if (CollectionUtil.isEmpty(list)) return new ArrayList<>();
        List<Long> ids = list.stream().map(ArticleCommentVo::getId).collect(Collectors.toList());
        //二级评论
        List<ArticleCommentVo> levelList = baseMapper.selectVoList(new LambdaQueryWrapper<ArticleComment>()
                .eq(ArticleComment::getArticleId, bo.getArticleId())
                .eq(ArticleComment::getType, 2)
                .in(ArticleComment::getParentId, ids)
                .eq(ArticleComment::getState, StatusEnums.NORMAL.getCode())
                .orderByAsc(ArticleComment::getCreateTime));

        Set<String> uids = new HashSet<>();
        list.forEach(item -> uids.add(item.getCommentUid()));
        if (CollectionUtil.isNotEmpty(levelList)) {
            levelList.forEach(item -> uids.add(item.getTargetUid()));
        }

        List<TripartiteUser> tripartiteUserVos = tripartiteUserMapper.selectList(new LambdaQueryWrapper<TripartiteUser>()
                .in(TripartiteUser::getUuid, uids));
        Map<String, TripartiteUser> userMpa = tripartiteUserVos.stream().collect(Collectors.toMap(TripartiteUser::getUuid, item -> item));
        Map<Long, List<ArticleCommentVo>> levelmap = new HashMap<>();

        for (ArticleCommentVo item : levelList) {
            List<ArticleCommentVo> articleCommentVos = levelmap.get(item.getParentId());
            if (articleCommentVos == null) articleCommentVos = new ArrayList<>();
            //填充用户信息
            TripartiteUser user = userMpa.get(item.getCommentUid());
            if (user != null) {
                item.setCommentName(user.getNickname());
                item.setCommentAvatar(user.getAvatar());
            }
            TripartiteUser targetUser = userMpa.get(item.getTargetUid());
            if (targetUser != null) {
                item.setTargetName(targetUser.getNickname());
                item.setTargetAvatar(targetUser.getAvatar());
            }
            articleCommentVos.add(item);
            levelmap.put(item.getParentId(), articleCommentVos);
        }

        //组装数据
        for (ArticleCommentVo record : list) {
            TripartiteUser user = userMpa.get(record.getCommentUid());
            if (user != null) {
                record.setCommentName(user.getNickname());
                record.setCommentAvatar(user.getAvatar());
            }
            List<ArticleCommentVo> dictumCommentVos = levelmap.get(record.getId());
            if (dictumCommentVos != null) {
                record.setMountComment(dictumCommentVos);
            }
        }
        list.get(0).setCommentTotal(levelList.size() + list.size());
        return list;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteWithValidById(ArticleCommentBo bo) {
        //        获取该评论的所以子集评论
        List<ArticleComment> articleComments = baseMapper.selectList(new QueryWrapper<ArticleComment>()
                .eq("parent_id", bo.getId()).or().eq("target_id", bo.getId()));
        List<Long> collect = articleComments.stream().map(item -> item.getId()).collect(Collectors.toList());
        collect.add(bo.getId());
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                //        异步更新数据
                commentDeletePreprocessing(bo, collect.size());
            }
        });
        return baseMapper.deleteBatchIds(collect) > 0;
    }

    @Async
    public void commentDeletePreprocessing(ArticleCommentBo bo, Integer size) {
        //        修改评论人评论次数
        countUserWebsiteMapper.updateDelete(bo.getCommentUid(), CountUserTypeEnums.COMMENT_COUNT.getCode());
        //        修改作品作者被评论数
        countUserWebsiteMapper.updateDelete(bo.getUid(), CountUserTypeEnums.FANS_COMMENT_COUNT.getCode());
        //修改文章评论数
        articleInformationMapper.updateDeleteNumber(bo.getArticleId(),
                ArticleUpdateTypeEnums.COMMENT_COUNT.getCode(), size);
    }

    @Override
    public R getComment(Long id) {
        NewsUserRecord newsUserRecord = newsUserRecordMapper.selectById(id);
        if (ObjectUtils.isEmpty(newsUserRecord)) {
            return R.ok(MsgEnums.COMMENT_DELETED.getValue());
        }
        return R.ok(newsUserRecord);
    }
}

