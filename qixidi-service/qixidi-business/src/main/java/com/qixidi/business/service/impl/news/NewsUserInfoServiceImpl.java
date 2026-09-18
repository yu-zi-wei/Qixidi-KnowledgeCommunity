package com.qixidi.business.service.impl.news;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.entity.news.NewsUserInfo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.DictumCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsSystemInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.qixidi.business.domain.vo.news.TimeNotesCommentNewsVo;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.qixidi.business.mapper.news.NewsUserInfoMapper;
import com.qixidi.business.mapper.privateUser.PrivateNewsInfoMapper;
import com.qixidi.business.service.news.INewsUserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
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

    private final NewsUserInfoMapper baseMapper;
    private final NewsSystemInfoMapper newsSystemInfoMapper;
    private final NewsUserRecordMapper newsUserRecordMapper;
    private final PrivateNewsInfoMapper privateNewsInfoMapper;

    /**
     * 查询用户消息
     *
     * @param id 用户消息主键
     * @return 用户消息
     */
    @Override
    public NewsUserInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户消息列表
     *
     * @param bo 用户消息
     * @return 用户消息
     */
    @Override
    public TableDataInfo<NewsUserInfoVo> queryPageList(NewsUserInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<NewsUserInfo> lqw = buildQueryWrapper(bo);
        Page<NewsUserInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户消息列表
     *
     * @param bo 用户消息
     * @return 用户消息
     */
    @Override
    public List<NewsUserInfoVo> queryList(NewsUserInfoBo bo) {
        LambdaQueryWrapper<NewsUserInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<NewsUserInfo> buildQueryWrapper(NewsUserInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<NewsUserInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getNewsContent()), NewsUserInfo::getNewsContent, bo.getNewsContent());
        lqw.eq(bo.getType() != null, NewsUserInfo::getType, bo.getType());
        lqw.eq(bo.getState() != null, NewsUserInfo::getState, bo.getState());
        lqw.eq(bo.getBeenRead() != null, NewsUserInfo::getBeenRead, bo.getBeenRead());
        lqw.eq(bo.getSenderId() != null, NewsUserInfo::getSenderId, bo.getSenderId());
        lqw.eq(bo.getRecipientId() != null, NewsUserInfo::getRecipientId, bo.getRecipientId());
        return lqw;
    }

    /**
     * 新增用户消息
     *
     * @param bo 用户消息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(NewsUserInfoBo bo) {
        NewsUserInfo add = BeanUtil.toBean(bo, NewsUserInfo.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户消息
     *
     * @param bo 用户消息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(NewsUserInfoBo bo) {
        NewsUserInfo update = BeanUtil.toBean(bo, NewsUserInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除用户消息
     *
     * @param ids 需要删除的用户消息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
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

        List<NewsUserRecord> systemNewsList = new ArrayList<>();
        newsSystemList.forEach(item -> {
            if (!cacheSet.contains(item.getId())) {
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
        IPage<ArticleCommentNewsVo> page = baseMapper.selectArticleNews(uuid, NewsType.COMMENT_NEWS.getCode(), pageQuery.build());
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<TimeNotesCommentNewsVo> timeNotesCommentList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<TimeNotesCommentNewsVo> page = baseMapper.selectTimeNotesCommentNews(uuid, NewsType.TIME_NOTES_COMMENT_NEWS.getCode(), pageQuery.build());
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<DictumCommentNewsVo> dictumCommentList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<DictumCommentNewsVo> page = baseMapper.selectDictumCommentNews(uuid, NewsType.DICTUM_COMMENT_NEWS.getCode(), pageQuery.build());
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<NewsUserInfoVo> fabulousList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<NewsUserInfoVo> page = baseMapper.selectFabulousNews(uuid, NewsType.FABULOUS_NEWS.getCode(), pageQuery.build());
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<NewsUserInfoVo> followList(PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        IPage<NewsUserInfoVo> page = baseMapper.selectFollowNews(uuid, NewsType.FOLLOW_NEWS.getCode(), pageQuery.build());
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
            if (CollectionUtils.isNotEmpty(newsSystemList)) {
                newsSystemList.forEach(item -> {
                    if (!cacheSet.contains(item.getId())) {
                        cacheSet.add(item.getId());
                    }
                });
            }
            RedisUtils.setCacheSet(key, cacheSet);
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
