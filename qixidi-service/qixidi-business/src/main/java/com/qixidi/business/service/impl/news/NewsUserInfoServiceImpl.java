package com.qixidi.business.service.impl.news;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.vo.CensusVo;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.light.redission.utils.RedisUtils;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.news.NewsUserRecordBo;
import com.qixidi.business.domain.bo.user.TripartiteUserBo;
import com.qixidi.business.domain.entity.comment.ArticleComment;
import com.qixidi.business.domain.entity.dictum.DictumComment;
import com.qixidi.business.domain.entity.news.NewsSystemRead;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.entity.timeNotes.TimeNotesComment;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.DictumCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsSystemInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.qixidi.business.domain.vo.news.NewsUserRecordVo;
import com.qixidi.business.domain.vo.news.TimeNotesCommentNewsVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.mapper.comment.ArticleCommentMapper;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.dictum.DictumCommentMapper;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.qixidi.business.mapper.news.NewsSystemReadMapper;
import com.qixidi.business.mapper.privateUser.PrivateNewsInfoMapper;
import com.qixidi.business.mapper.timeNotes.TimeNotesCommentMapper;
import com.qixidi.business.service.ITripartiteUserService;
import com.qixidi.business.service.news.INewsUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 用户消息Service业务层处理
 *
 * @author aurora
 * @date 2022-11-03
 */
@RequiredArgsConstructor
@Service
public class NewsUserInfoServiceImpl implements INewsUserInfoService {

    private final NewsSystemInfoMapper newsSystemInfoMapper;
    private final NewsSystemReadMapper newsSystemReadMapper;
    private final NewsUserRecordMapper newsUserRecordMapper;
    private final PrivateNewsInfoMapper privateNewsInfoMapper;
    private final ArticleCommentMapper articleCommentMapper;
    private final DictumCommentMapper dictumCommentMapper;
    private final TimeNotesCommentMapper timeNotesCommentMapper;
    private final ITripartiteUserService tripartiteUserService;

    /**
     * 后台管理：用户消息记录分页（b_news_user_record 单表查询，发送者/接收者展示字段由 Service 组装，筛选列必须带 nur 别名）
     */
    @Override
    public TableDataInfo<NewsUserRecordVo> recordPage(NewsUserRecordBo bo, PageQuery pageQuery) {
        QueryWrapper<NewsUserRecord> wrapper = Wrappers.query();
        wrapper.in(CollectionUtils.isNotEmpty(bo.getTypes()), "nur.type", bo.getTypes());
        if (StringUtils.isNotBlank(bo.getUsername())) {
            String keyword = bo.getUsername().trim();
            // 发送者/接收者按昵称或用户名匹配：用户域查出 uuid 集合后 in（等价原 join us/ur 两侧 like 的 OR 语义）
            Set<String> matched = new HashSet<>();
            TripartiteUserBo userBo = new TripartiteUserBo();
            userBo.setUsername(keyword);
            tripartiteUserService.queryList(userBo).forEach(u -> matched.add(u.getUuid()));
            userBo.setUsername(null);
            userBo.setNickname(keyword);
            tripartiteUserService.queryList(userBo).forEach(u -> matched.add(u.getUuid()));
            if (matched.isEmpty()) {
                return TableDataInfo.build(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
            }
            wrapper.and(w -> w.in("nur.target_uid", matched).or().in("nur.uid", matched));
        }
        wrapper.orderByDesc("nur.create_time");
        IPage<NewsUserRecordVo> result = newsUserRecordMapper.selectSystemPage(pageQuery.build(), wrapper);
        fillCommentContent(result.getRecords());
        fillRecordUserDisplay(result.getRecords());
        return TableDataInfo.build(result);
    }

    /**
     * 回填评论正文：content 是冗余字段仅点赞写入被赞对象标题；评论正文按类型批量查对应评论表在业务层组装
     * （SQL 不跨业务域连表），源评论已删除的行正文为空（前端空值占位）
     */
    private void fillCommentContent(List<NewsUserRecordVo> records) {
        Map<Integer, Map<Long, String>> commentContents = new HashMap<>();
        commentContents.put(NewsType.COMMENT_NEWS.getCode(),
            queryContents(newsIdsOfType(records, NewsType.COMMENT_NEWS.getCode()),
                articleCommentMapper::selectBatchIds, ArticleComment::getId, ArticleComment::getContent));
        commentContents.put(NewsType.DICTUM_COMMENT_NEWS.getCode(),
            queryContents(newsIdsOfType(records, NewsType.DICTUM_COMMENT_NEWS.getCode()),
                dictumCommentMapper::selectBatchIds, DictumComment::getId, DictumComment::getContent));
        commentContents.put(NewsType.TIME_NOTES_COMMENT_NEWS.getCode(),
            queryContents(newsIdsOfType(records, NewsType.TIME_NOTES_COMMENT_NEWS.getCode()),
                timeNotesCommentMapper::selectBatchIds, TimeNotesComment::getId, TimeNotesComment::getContent));
        records.forEach(r -> {
            Map<Long, String> contents = commentContents.get(r.getType());
            if (contents != null && r.getNewsId() != null && contents.containsKey(r.getNewsId())) {
                r.setContent(contents.get(r.getNewsId()));
            }
        });
    }

    private List<Long> newsIdsOfType(List<NewsUserRecordVo> records, Integer type) {
        return records.stream()
            .filter(r -> type.equals(r.getType()) && r.getNewsId() != null)
            .map(NewsUserRecordVo::getNewsId)
            .collect(Collectors.toList());
    }

    /**
     * 批量查评论表转 id->正文 映射（正文空的评论不参与映射，避免 toMap 空值）
     */
    private <T> Map<Long, String> queryContents(List<Long> ids, Function<List<Long>, List<T>> batchQuery,
                                                Function<T, Long> idGetter, Function<T, String> contentGetter) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return batchQuery.apply(ids).stream()
            .filter(c -> StringUtils.isNotBlank(contentGetter.apply(c)))
            .collect(Collectors.toMap(idGetter, contentGetter, (a, b) -> a));
    }

    /**
     * 组装后台用户消息的发送者/接收者昵称头像（键 senderUid/recipientUid）
     */
    private void fillRecordUserDisplay(List<NewsUserRecordVo> records) {
        Set<String> uids = new HashSet<>();
        records.forEach(r -> {
            if (StringUtils.isNotBlank(r.getSenderUid())) {
                uids.add(r.getSenderUid());
            }
            if (StringUtils.isNotBlank(r.getRecipientUid())) {
                uids.add(r.getRecipientUid());
            }
        });
        Map<String, TripartiteUserVo> users = userMap(uids);
        records.forEach(r -> {
            TripartiteUserVo sender = users.get(r.getSenderUid());
            if (sender != null) {
                r.setSenderName(sender.getNickname());
                r.setSenderAvatar(sender.getAvatar());
            }
            TripartiteUserVo recipient = users.get(r.getRecipientUid());
            if (recipient != null) {
                r.setRecipientName(recipient.getNickname());
                r.setRecipientAvatar(recipient.getAvatar());
            }
        });
    }

    /**
     * 组装评论消息的评论人昵称头像（键 commentUid；三个评论消息 VO 字段同名但无公共父类，展示字段写入由调用方传入）
     */
    private <T> void fillCommenterDisplay(List<T> records, Function<T, String> commentUidGetter,
                                          BiConsumer<T, TripartiteUserVo> displaySetter) {
        List<String> uids = records.stream()
            .map(commentUidGetter)
            .filter(StringUtils::isNotBlank)
            .distinct()
            .collect(Collectors.toList());
        Map<String, TripartiteUserVo> users = userMap(uids);
        records.forEach(r -> {
            TripartiteUserVo u = users.get(commentUidGetter.apply(r));
            if (u != null) {
                displaySetter.accept(r, u);
            }
        });
    }

    /**
     * 存活用户 Map（uuid -> 用户，state=0）
     */
    private Map<String, TripartiteUserVo> aliveUserMap() {
        TripartiteUserBo bo = new TripartiteUserBo();
        bo.setState(0);
        return tripartiteUserService.queryList(bo).stream()
            .collect(Collectors.toMap(TripartiteUserVo::getUuid, Function.identity(), (a, b) -> a));
    }

    /**
     * 用户展示字段 Map（uuid -> 用户），跨业务域展示字段组装统一走用户域 getUserByUids
     */
    private Map<String, TripartiteUserVo> userMap(Collection<String> uids) {
        if (CollectionUtils.isEmpty(uids)) {
            return Collections.emptyMap();
        }
        return tripartiteUserService.getUserByUids(uids).stream()
            .collect(Collectors.toMap(TripartiteUserVo::getUuid, Function.identity(), (a, b) -> a));
    }

    /**
     * 后台管理：批量删除用户消息记录
     */
    @Override
    public Boolean deleteRecordByIds(Collection<Long> ids) {
        return newsUserRecordMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public List<NewsUserSumVo> listSum() {
        String uid = LoginHelper.getTripartiteUuid();
        return listSums(uid);
    }

    public List<NewsUserSumVo> listSums(String uid) {
        List<NewsUserSumVo> list = new ArrayList<>();
        for (NewsType value : NewsType.values()) {
            if (isCommentSubType(value)) {
                continue;//评论子类型（随笔/小记）不单独展示，未读数合并进"评论"
            }
            NewsUserSumVo newsUserSumVo = new NewsUserSumVo().setType(value.getCode()).setTypeInfo(value.getValue())
                    .setNewsSum(0).setRoute(value.getRoute());
            list.add(newsUserSumVo);
        }
        List<NewsUserRecord> newsUserRecords = newsUserRecordMapper.selectLists(uid);

        Map<Integer, List<NewsUserRecord>> collect = new HashMap<>();
        if (ObjectUtils.isNotEmpty(uid) || CollectionUtils.isNotEmpty(newsUserRecords)) {
            collect = newsUserRecords.stream().collect(Collectors.groupingBy(NewsUserRecord::getType));
        }
        //查询用户未读私信列表
        List<NewsUserRecord> newsUserInfos = privateNewsInfoMapper.selectRead(uid);
        if (CollectionUtils.isNotEmpty(newsUserInfos)) {
            collect.put(NewsType.PRIVATE_LETTER.getCode(), newsUserInfos);
        }

//        查询系统消息
//        Set<String> cacheSet = RedisUtils.getCacheSet(RedisKeyEnums.SYSTEM_MESSAGES_READ_LIST.getKey());
        List<NewsSystemInfoVo> newsSystemList = newsSystemInfoMapper.selectBaseUid(uid);
        String key = String.format(RedisBusinessKeyEnums.USER_SYSTEM_MESSAGES.getKey(), uid);
        Set<Long> cacheSet = RedisUtils.getCacheSet(key);
        if (cacheSet.isEmpty()) {
            // Redis 已读集为空（数据丢失或新用户）：从已读落库表回灌，库为权威源
            List<Long> readIds = newsSystemReadMapper.selectNewsIdsByUid(uid);
            if (!readIds.isEmpty()) {
                cacheSet = new HashSet<>(readIds);
                RedisUtils.setCacheSet(key, cacheSet);
            }
        }
        // 回灌分支存在重赋值，lambda 引用需 final 变量承接
        final Set<Long> readSet = cacheSet;

        List<NewsUserRecord> systemNewsList = new ArrayList<>();
        newsSystemList.forEach(item -> {
            if (!readSet.contains(item.getId())) {
                NewsUserRecord newsUserInfo = new NewsUserRecord();
                newsUserInfo.setId(item.getId());
                systemNewsList.add(newsUserInfo);
            }
        });
        if (CollectionUtils.isNotEmpty(systemNewsList)) {
            collect.put(NewsType.SYSTEM_NEWS.getCode(), systemNewsList);
        }

        //随笔/小记评论是"评论"的子类型：先取出各自分项数量（供二级 tab 红点），再把未读记录合并进"评论"
        List<NewsUserRecord> dictumRecords = collect.remove(NewsType.DICTUM_COMMENT_NEWS.getCode());
        List<NewsUserRecord> timeNotesRecords = collect.remove(NewsType.TIME_NOTES_COMMENT_NEWS.getCode());
        List<NewsUserRecord> articleRecords = collect.get(NewsType.COMMENT_NEWS.getCode());
        int articleUnread = CollectionUtils.isEmpty(articleRecords) ? 0 : articleRecords.size();
        int dictumUnread = CollectionUtils.isEmpty(dictumRecords) ? 0 : dictumRecords.size();
        int timeNotesUnread = CollectionUtils.isEmpty(timeNotesRecords) ? 0 : timeNotesRecords.size();
        List<NewsUserRecord> commentRecords = collect.computeIfAbsent(NewsType.COMMENT_NEWS.getCode(), k -> new ArrayList<>());
        if (CollectionUtils.isNotEmpty(dictumRecords)) {
            commentRecords.addAll(dictumRecords);
        }
        if (CollectionUtils.isNotEmpty(timeNotesRecords)) {
            commentRecords.addAll(timeNotesRecords);
        }

        Map<Integer, List<NewsUserRecord>> finalCollect = collect;
        list.forEach(item -> {
            if (finalCollect.get(item.getType()) != null) {
                item.setNewsSum(finalCollect.get(item.getType()).size());
            }
        });

        //评论条目附加子类型分项（文章/小记/随笔各自未读数，前端二级 tab 红点用）
        list.stream()
                .filter(item -> NewsType.COMMENT_NEWS.getCode().equals(item.getType()))
                .findFirst()
                .ifPresent(item -> item.setSubList(Arrays.asList(
                        new NewsUserSumVo().setType(NewsType.COMMENT_NEWS.getCode()).setTypeInfo("文章")
                                .setNewsSum(articleUnread).setRoute(item.getRoute()),
                        new NewsUserSumVo().setType(NewsType.TIME_NOTES_COMMENT_NEWS.getCode()).setTypeInfo("小记")
                                .setNewsSum(timeNotesUnread).setRoute(item.getRoute()),
                        new NewsUserSumVo().setType(NewsType.DICTUM_COMMENT_NEWS.getCode()).setTypeInfo("随笔")
                                .setNewsSum(dictumUnread).setRoute(item.getRoute())
                )));
        return list;
    }

    @Override
    public TableDataInfo<ArticleCommentNewsVo> commentList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<ArticleCommentNewsVo> page = newsUserRecordMapper.selectArticleNews(uuid, NewsType.COMMENT_NEWS.getCode(), pageQuery.build());
        fillCommenterDisplay(page.getRecords(), ArticleCommentNewsVo::getCommentUid,
            (r, u) -> { r.setCommentName(u.getNickname()); r.setCommentAvatar(u.getAvatar()); });
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<TimeNotesCommentNewsVo> timeNotesCommentList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<TimeNotesCommentNewsVo> page = newsUserRecordMapper.selectTimeNotesCommentNews(uuid, NewsType.TIME_NOTES_COMMENT_NEWS.getCode(), pageQuery.build());
        fillCommenterDisplay(page.getRecords(), TimeNotesCommentNewsVo::getCommentUid,
            (r, u) -> { r.setCommentName(u.getNickname()); r.setCommentAvatar(u.getAvatar()); });
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<DictumCommentNewsVo> dictumCommentList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<DictumCommentNewsVo> page = newsUserRecordMapper.selectDictumCommentNews(uuid, NewsType.DICTUM_COMMENT_NEWS.getCode(), pageQuery.build());
        fillCommenterDisplay(page.getRecords(), DictumCommentNewsVo::getCommentUid,
            (r, u) -> { r.setCommentName(u.getNickname()); r.setCommentAvatar(u.getAvatar()); });
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<NewsUserInfoVo> fabulousList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        return listSenderNews(uuid, NewsType.FABULOUS_NEWS.getCode(), pageQuery);
    }

    @Override
    public TableDataInfo<NewsUserInfoVo> followList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        return listSenderNews(uuid, NewsType.FOLLOW_NEWS.getCode(), pageQuery);
    }

    /**
     * 点赞/关注消息分页：存活用户集合既作过滤参数（等价原 join um.state=0，被冻结/注销用户的消息不展示）也承载发送者展示字段
     */
    private TableDataInfo<NewsUserInfoVo> listSenderNews(String uid, Integer type, PageQuery pageQuery) {
        Map<String, TripartiteUserVo> aliveUsers = aliveUserMap();
        if (aliveUsers.isEmpty()) {
            return TableDataInfo.build(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
        }
        IPage<NewsUserInfoVo> page = type.equals(NewsType.FABULOUS_NEWS.getCode())
            ? newsUserRecordMapper.selectFabulousNews(uid, type, new ArrayList<>(aliveUsers.keySet()), pageQuery.build())
            : newsUserRecordMapper.selectFollowNews(uid, type, new ArrayList<>(aliveUsers.keySet()), pageQuery.build());
        page.getRecords().forEach(r -> {
            TripartiteUserVo sender = aliveUsers.get(r.getSenderId());
            if (sender != null) {
                r.setSenderName(sender.getNickname());
                r.setSenderAvatar(sender.getAvatar());
            }
        });
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<NewsUserInfoVo> systemList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<NewsUserInfoVo> page = newsSystemInfoMapper.selectUid(uuid, pageQuery.build());
        return TableDataInfo.build(page);
    }

    @Override
    public List<NewsUserSumVo> pushOne(String userid) {
        return listSums(userid);
    }

    @Override
    public boolean newsRead(NewsUserRecord bo) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (bo.getType() == NewsType.SYSTEM_NEWS.getCode()) {
            List<NewsSystemInfoVo> newsSystemList = newsSystemInfoMapper.selectBase();
            String key = String.format(RedisBusinessKeyEnums.USER_SYSTEM_MESSAGES.getKey(), uuid);
            Set<Long> cacheSet = RedisUtils.getCacheSet(key);
            Date now = new Date();
            List<NewsSystemRead> readRecords = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(newsSystemList)) {
                newsSystemList.forEach(item -> {
                    if (!cacheSet.contains(item.getId())) {
                        cacheSet.add(item.getId());
                        // 双写落库：Redis 丢失时的回灌数据源
                        readRecords.add(new NewsSystemRead().setUid(uuid).setNewsId(item.getId()).setCreateTime(now));
                    }
                });
            }
            RedisUtils.setCacheSet(key, cacheSet);
            if (!readRecords.isEmpty()) {
                // INSERT IGNORE + uk_uid_news：重复标记/并发下幂等
                newsSystemReadMapper.insertIgnoreBatch(readRecords);
            }
        }
        newsUserRecordMapper.update(new LambdaUpdateWrapper<NewsUserRecord>()
                .set(NewsUserRecord::getBeenRead, bo.getBeenRead())
                .eq(NewsUserRecord::getType, bo.getType())
                .eq(NewsUserRecord::getUid, uuid)
        );
        //WebSocket推送消息
        WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(uuid);
        return true;
    }

    @Override
    public List<NewsUserSumVo> listInfo() {
        List<NewsUserSumVo> list = new ArrayList<>();
        for (NewsType value : NewsType.values()) {
            if (isCommentSubType(value)) {
                continue;//评论子类型（随笔/小记）不单独展示
            }
            NewsUserSumVo newsUserSumVo = new NewsUserSumVo().setType(value.getCode()).setTypeInfo(value.getValue())
                    .setNewsSum(0).setRoute(value.getRoute());
            list.add(newsUserSumVo);
        }
        return list;
    }

    /**
     * 是否为评论子类型（随笔/小记评论，未读数归属"评论"类型，不单独展示）
     */
    private boolean isCommentSubType(NewsType type) {
        return NewsType.DICTUM_COMMENT_NEWS.getCode().equals(type.getCode())
                || NewsType.TIME_NOTES_COMMENT_NEWS.getCode().equals(type.getCode());
    }

    /**
     * 近30天互动趋势（获赞/评论按天聚合，评论子类型 6/7 并入评论）
     */
    @Override
    public List<CensusVo> interactTrend() {
        String uuid = LoginHelper.getTripartiteUuid();
        String time = DateUtil.formatDate(DateUtil.offsetDay(new Date(), -30));
        return newsUserRecordMapper.selectInteractTrend(uuid, time);
    }
}
