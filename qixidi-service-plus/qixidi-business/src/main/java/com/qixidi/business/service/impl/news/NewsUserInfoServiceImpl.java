package com.qixidi.business.service.impl.news;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.news.NewsUserInfoBo;
import com.qixidi.business.domain.entity.news.NewsUserInfo;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.vo.news.ArticleCommentNewsVo;
import com.qixidi.business.domain.vo.news.NewsSystemInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserInfoVo;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.qixidi.business.mapper.news.NewsUserInfoMapper;
import com.qixidi.business.mapper.privateUser.PrivateNewsInfoMapper;
import com.qixidi.business.selector.webSocket.WebSocketSelector;
import com.qixidi.business.service.news.INewsUserInfoService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.RedisKeyEnums;
import com.light.core.enums.WebSocketEnum;
import com.light.core.enums.news.NewsType;
import com.qixidi.auth.helper.LoginHelper;
import com.light.core.utils.StringUtils;
import com.light.redission.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        String key = String.format(RedisKeyEnums.USER_SYSTEM_MESSAGES.getKey(), uid);
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

        Map<Integer, List<NewsUserRecord>> finalCollect = collect;
        list.forEach(item -> {
            if (finalCollect.get(item.getType()) != null) {
                item.setNewsSum(finalCollect.get(item.getType()).size());
            }
        });
        return list;
    }

    @Override
    public Object userList(NewsUserInfoBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        //系统消息
        Page<Object> build = pageQuery.build();
        if (bo.getType().equals(NewsType.SYSTEM_NEWS.getCode())) {
            IPage<NewsUserInfoVo> newsUserInfoVoIPages = newsSystemInfoMapper.selectUid(uuid, pageQuery.build());
            return newsUserInfoVoIPages;
        }
        // 评论消息
        if (bo.getType().equals(NewsType.COMMENT_NEWS.getCode())) {
            IPage<ArticleCommentNewsVo> news = baseMapper.selectArticleNews(uuid, NewsType.COMMENT_NEWS.getCode(), build);
            return news;
        }
        //点赞消息
        if (bo.getType().equals(NewsType.FABULOUS_NEWS.getCode())) {
            IPage<NewsUserInfoVo> news = baseMapper.selectFabulousNews(uuid, NewsType.FABULOUS_NEWS.getCode(), build);
            return news;
        }
        //关注消息
        if (bo.getType().equals(NewsType.FOLLOW_NEWS.getCode())) {
            IPage<NewsUserInfoVo> news = baseMapper.selectFollowNews(uuid, NewsType.FOLLOW_NEWS.getCode(), pageQuery.build());
            return news;
        }
        return null;
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
            String key = String.format(RedisKeyEnums.USER_SYSTEM_MESSAGES.getKey(), uuid);
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
        newsUserRecordMapper.update(null, new UpdateWrapper<NewsUserRecord>()
            .set("been_read", bo.getBeenRead())
            .eq("type", bo.getType())
            .eq("uid", uuid)
        );
        //WebSocket推送消息
        WebSocketSelector.execute(uuid, WebSocketEnum.INSIDE_NOTICE);
        return true;
    }

    @Override
    public List<NewsUserSumVo> listInfo() {
        List<NewsUserSumVo> list = new ArrayList<>();
        for (NewsType value : NewsType.values()) {
            NewsUserSumVo newsUserSumVo = new NewsUserSumVo().setType(value.getCode()).setTypeInfo(value.getValue())
                .setNewsSum(0).setRoute(value.getRoute());
            list.add(newsUserSumVo);
        }
        return list;
    }
}
