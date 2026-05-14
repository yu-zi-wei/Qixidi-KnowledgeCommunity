package com.qixidi.business.service.impl.article;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.ai.service.DeepSeekService;
import com.light.core.constant.SystemConstant;
import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.vo.CensusVo;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.MsgEnums;
import com.light.core.utils.AlgorithmUtils;
import com.light.core.utils.DateUtils;
import com.light.core.utils.EscapeUtil;
import com.light.core.utils.StringUtils;
import com.light.core.utils.email.MailUtils;
import com.light.core.utils.ip.AddressUtils;
import com.light.core.utils.word.WordFilter;
import com.light.exception.ServiceException;
import com.light.redission.utils.RedisUtils;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserRoleEnums;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.event.ArticleEvent;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.ArticleInformationTwoBo;
import com.qixidi.business.domain.bo.article.SortTypeBo;
import com.qixidi.business.domain.entity.SearchRecords;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.collection.CollectionRecord;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.entity.news.NewsSystemInfo;
import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.domain.enums.CollectionTypeEnums;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.UserFollowTypeEnums;
import com.qixidi.business.domain.enums.article.ArticleAuditStateEnums;
import com.qixidi.business.domain.enums.article.ArticleUpdateTypeEnums;
import com.qixidi.business.domain.vo.article.ArticleArchiveVo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.mapper.SearchRecordsMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.qixidi.business.mapper.shield.ToShieldWordMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.qixidi.business.service.article.IArticleInformationService;
import com.qixidi.business.service.count.ArticleCountQueryHelper;
import com.qixidi.business.service.count.UserCountQueryHelper;
import com.qixidi.common.domain.enums.StatusEnums;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 文章信息Service业务层处理
 *
 * @author aurora
 * @date 2022-08-16
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleInformationServiceImpl implements IArticleInformationService {

    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;

    private final ApplicationEventPublisher eventPublisher;

    private final ArticleInformationMapper baseMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final LabelGroupingInfoMapper labelGroupingInfoMapper;
    private final CollectionRecordMapper collectionRecordMapper;
    private final UserFollowMapper userFollowMapper;
    private final SearchRecordsMapper searchRecordsMapper;
    private final ToShieldWordMapper toShieldWordMapper;
    private final NewsSystemInfoMapper newsSystemInfoMapper;
    private final DeepSeekService deepSeekService;
    private final ArticleCountQueryHelper articleCountQueryHelper;
    private final UserCountQueryHelper userCountQueryHelper;

    /**
     * 查询文章信息
     *
     * @param id 文章信息主键
     * @return 文章信息
     */
    @Override
    public ArticleInformationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }


    /**
     * 查询文章信息列表
     *
     * @param bo 文章信息
     * @return 文章信息
     */
    @Override
    public List<ArticleInformationVo> queryList(ArticleInformationBo bo) {
        QueryWrapper<ArticleInformation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private QueryWrapper<ArticleInformation> buildQueryWrapper(ArticleInformationBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<ArticleInformation> lqw = Wrappers.query();
        lqw.le("ai.type", 10);
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), "um.nickname", bo.getNickname());
        lqw.like(StringUtils.isNotBlank(bo.getArticleTitle()), "ai.article_title", bo.getArticleTitle());
        lqw.like(StringUtils.isNotBlank(bo.getLabelId()), "ai.label_id", bo.getLabelId());
        lqw.eq(bo.getGroupingId() != null, "ai.grouping_id", bo.getGroupingId());
        lqw.eq(bo.getType() != null, "ai.type", bo.getType());
        lqw.eq(bo.getAuditState() != null, "ai.audit_state", bo.getAuditState());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                "ai.create_time", params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc("ai.create_time");
        return lqw;
    }

    /**
     * 新增文章信息
     *
     * @param bo 文章信息
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleInformationVo insertByBo(ArticleInformationBo bo) {
        ArticleInformation add = BeanUtil.toBean(bo, ArticleInformation.class);
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        String uuid = tripartiteUser.getUuid();
        if (!UserRoleEnums.getAdvancedRoleList().contains(tripartiteUser.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        ArticleInformationVo vo = new ArticleInformationVo();
        add.setUserId(uuid);
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        baseMapper.insert(add);
        Long id = add.getId();
        vo.setId(id);
        if (bo.getAuditState().equals(ArticleAuditStateEnums.DRAFT.getCode())) return vo;

        // 发布事件，事务提交后异步执行
        eventPublisher.publishEvent(new ArticleEvent(
                id, add.getArticleTitle(), add.getArticleContent(),
                bo.getArticleAbstract(), uuid,
                bo.getAbstractSelect() != null && bo.getAbstractSelect(),
                true
        ));

        return vo;
    }

    @Override
    public void handleArticleAsync(ArticleEvent event) {
        Long id = event.getArticleId();
        //计算文章推荐权重
        ArticleInformationVo vo = new ArticleInformationVo();
        vo.setId(id);
        vo.setCreateTime(new Date());
        articleWeightAlgorithms(List.of(vo));
        //生成 AI 总结
        aiSummary(id, event.getArticleTitle(), event.getArticleContent());
        //生成 AI 摘要
        if (event.isGenerateAbstract()) {
            aiAbstract(id, event.getArticleTitle(), event.getArticleContent());
        }
        //文章自动审核，发送消息
        if (event.isNeedReview()) {
            articleReview(event.getArticleTitle(), event.getArticleContent(),
                    event.getArticleAbstract(), id, event.getAuthorUuid());
        }
    }


    public Integer articleReview(String Title, String Content, String Abstract, Long id, String uuid) {
        //文章审核
        List<String> cacheList = RedisUtils.getCacheList(RedisBusinessKeyEnums.BLOCKING_WORDS.getKey());
        if (CollectionUtils.isEmpty(cacheList)) {
            //        存入缓存
            List<String> stringList = toShieldWordMapper.selectKeyword();
            RedisUtils.setCacheList(RedisBusinessKeyEnums.BLOCKING_WORDS.getKey(), stringList);
            cacheList = stringList;
        }
        WordFilter wordFilter = new WordFilter(cacheList);
        int wordCount = wordFilter.wordCount(Title);
        int wordCount1 = wordFilter.wordCount(Content);
        int wordCount2 = 0;
        if (StrUtil.isNotBlank(Abstract)) {
            wordCount2 = wordFilter.wordCount(Abstract);
        }
        if (wordCount > 10 || wordCount1 > 10 || wordCount2 > 10) {
            //            发送人工审核消息
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "文章自动审核失败",
                    String.format("文章名称：%s，标题触发敏感词数：%s，内容触发敏感词数：%s，摘要触发敏感词数：%s",
                            Title, wordCount, wordCount1, wordCount2));
            return ArticleAuditStateEnums.FAILED_TO_PASS_REVIEW.getCode();
        }
//                修改文章状态
        baseMapper.update(new LambdaUpdateWrapper<ArticleInformation>()
                .set(ArticleInformation::getAuditState,
                        ArticleAuditStateEnums.APPROV.getCode()).set(ArticleInformation::getAuditTime, new Date())
                .eq(ArticleInformation::getId, id));
//        发送消息
        NewsSystemInfo newsSystemInfo = new NewsSystemInfo()
                .setNewsTitle("你的文章《" + Title + "》已审核通过！")
                .setNewsContent("你的文章《" + Title + "》已审核通过！")
                .setIsDetails(1L)
                .setType(2L)
                .setIsMassAir(2L)
                .setUid(uuid)
                .setCreateTime(new Date());
        newsSystemInfoMapper.insert(newsSystemInfo);
        //WebSocket推送消息
        WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(uuid);
        return ArticleAuditStateEnums.APPROV.getCode();
    }

    public void articleWeightAlgorithms(List<ArticleInformationVo> list) {
        List<ArticleInformation> heatWeightList = list.stream().map(item -> {
            ArticleInformation articleInformation = new ArticleInformation();
            articleInformation.setId(item.getId());
            Map<String, Integer> datePoor = DateUtils.getDatePoor(item.getCreateTime(), new Date());
            Integer day = datePoor.get("day");
            double heatWeight = (item.getLikeTimes() == null ? 0 : item.getLikeTimes())
                    + (item.getCommentTimes() == null ? 0 : item.getCommentTimes() * 2)
                    + (item.getCollectionTimes() == null ? 0 : item.getCollectionTimes() * 2)
                    + (item.getNumberTimes() == null ? 0 : item.getNumberTimes())
                    + (AlgorithmUtils.directionExport(day));
            articleInformation.setHeatWeight(heatWeight);
            return articleInformation;
        }).collect(Collectors.toList());
//        同步数据库
        Integer i = baseMapper.updateListHeatWeight(heatWeightList);
    }

    /**
     * 修改文章信息
     *
     * @param bo 文章信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ArticleInformationVo updateByBo(ArticleInformationBo bo) {
        ArticleInformation update = BeanUtil.toBean(bo, ArticleInformation.class);
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        String uuid = tripartiteUser.getUuid();
        if (!UserRoleEnums.getAdvancedRoleList().contains(tripartiteUser.getRoleId())) {
            throw new ServiceException(MsgEnums.NOT_CREATOR);
        }
        update.setUpdateId(uuid);
        update.setUpdateTime(new Date());
        boolean shouldReview = !ArticleAuditStateEnums.DRAFT.getCode().equals(bo.getAuditState());
        if (shouldReview) {
            update.setCreateTime(new Date());
        }
        if (baseMapper.updateById(update) > 0) {
            ArticleInformationVo articleInformationVo = new ArticleInformationVo();
            articleInformationVo.setId(bo.getId());
            // 发布事件，事务提交后异步执行
            eventPublisher.publishEvent(new ArticleEvent(
                    update.getId(), update.getArticleTitle(), update.getArticleContent(),
                    bo.getArticleAbstract(), uuid,
                    bo.getAbstractSelect() != null && bo.getAbstractSelect(),
                    shouldReview
            ));
            return articleInformationVo;
        }
        return new ArticleInformationVo();
    }

    /**
     * 生成ai摘要
     *
     * @param id
     * @param articleTitle
     * @param articleContent
     */
    private void aiAbstract(Long id, String articleTitle, String articleContent) {
        log.info("开始【AI 生成摘要】:" + id);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n 文章标题：" + articleTitle);
        stringBuffer.append("\n 文章内容：" + articleContent);
        stringBuffer.append("\n 要求：对该文章内容生成简单的文章摘要，要求纯文字返回，不要返回markdown格式，且不超过400个字符。");
        Object Summary = deepSeekService.generationContent(stringBuffer.toString());
        if (Summary != null) {
            baseMapper.update(new LambdaUpdateWrapper<ArticleInformation>()
                    .set(ArticleInformation::getArticleAbstract, Summary.toString())
                    .eq(ArticleInformation::getId, id));
            log.info("【AI 获取摘要】:" + id + ":" + Summary.toString());
        }
    }

    /**
     * AI 生成总结
     *
     * @param id
     * @param articleTitle
     * @param articleContent
     */
    private void aiSummary(Long id, String articleTitle, String articleContent) {
        log.info("开始【AI 生成总结】:" + id);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n 文章标题：" + articleTitle);
        stringBuffer.append("\n 文章内容：" + articleContent);
        stringBuffer.append("\n 要求：对该文章内容生成简单的总结，只需要总结这篇文章的大概内容，不需要详细总结。要求纯文字返回，不要返回markdown格式，且不超过400个字符。");
        Object Summary = deepSeekService.generationContent(stringBuffer.toString());
        if (Summary != null) {
            baseMapper.update(new LambdaUpdateWrapper<ArticleInformation>()
                    .set(ArticleInformation::getArticleSummary, Summary.toString())
                    .eq(ArticleInformation::getId, id));
            log.info("【AI 获取总结】:" + id + ":" + Summary.toString());
        }
    }

    /**
     * 批量删除文章信息
     *
     * @param ids 需要删除的文章信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<ArticleInformationVo> index(ArticleInformationBo bo, PageQuery pageQuery) {
        QueryWrapper<ArticleInformation> wrapper = buildQueryWrapper(bo);
        List<LabelInfoVo> list = labelInfoMapper.selectSimpleList();
        IPage<ArticleInformationVo> index = baseMapper.selectIndex(pageQuery.build(), wrapper);
        Map<String, String> labelMap = list.stream().collect(Collectors.toMap(item -> item.getId().toString(), LabelInfoVo::getLabelName));
        index.getRecords().forEach(item -> {
            if (StringUtils.isNotBlank(item.getLabelId())) {
                List<String> list1 = new ArrayList();
                String[] split = item.getLabelId().split(",");
                for (String s : split) {
                    if (labelMap.get(s) != null) {
                        list1.add(labelMap.get(s));
                    }
                }
                item.setLabelNameList(list1);
            }
        });
        enrichWithRealTimeCounts(index.getRecords());
        return TableDataInfo.build(index);
    }

    @Override
    public IPage<ArticleInformationVo> sortIndex(SortTypeBo bo, PageQuery pageQuery) {
        IPage<ArticleInformationVo> page = baseMapper.selectTypeSort(bo, pageQuery.build());
        enrichWithRealTimeCounts(page.getRecords());
        return page;
    }

    @Override
    public IPage<ArticleInformationVo> articleList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid != null || bo.getArticleTitle() != null) {
            executorService.execute(() -> {
                SearchRecords searchRecordsInfo = searchRecordsMapper.selectOne(new LambdaQueryWrapper<SearchRecords>()
                        .eq(SearchRecords::getUid, uuid)
                        .eq(SearchRecords::getContent, bo.getArticleTitle()));
                if (searchRecordsInfo != null) {
                    searchRecordsInfo.setCreateTime(new Date());
                    searchRecordsMapper.updateById(searchRecordsInfo);
                } else {
                    SearchRecords searchRecords = new SearchRecords();
                    searchRecords.setUid(uuid);
                    searchRecords.setCreateTime(new Date());
                    searchRecords.setContent(bo.getArticleTitle());
                    searchRecordsMapper.insert(searchRecords);
                }
            });
        }
        bo.setArticleTitle(EscapeUtil.escapeChar(bo.getArticleTitle()));
        IPage<ArticleInformationVo> page = baseMapper.articleList(bo, pageQuery.build());
        enrichWithRealTimeCounts(page.getRecords());
        return page;
    }

    @Override
    public IPage<ArticleInformationVo> articleRecommendList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid != null && bo.getArticleTitle() != null) {
            executorService.execute(() -> {
                SearchRecords searchRecords = new SearchRecords();
                searchRecords.setUid(uuid);
                searchRecords.setCreateTime(new Date());
                searchRecords.setContent(bo.getArticleTitle());
                searchRecordsMapper.insert(searchRecords);
            });
        }
        Set<Object> labelSet = RedisUtils.getZset(String.format(RedisBusinessKeyEnums.ARTICLE_INTIMACY.getKey(), uuid), 0, 10);
        String result = StringUtils.join(labelSet, ", ");
        IPage<ArticleInformationVo> page = baseMapper.articleRecommendList(bo, result, pageQuery.build());
        enrichWithRealTimeCounts(page.getRecords());
        return page;
    }

    @Override
    public TableDataInfo<ArticleInformationVo> latelyArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        IPage<ArticleInformationVo> page = baseMapper.latelyArticleList(bo, pageQuery.build());
        enrichWithRealTimeCounts(page.getRecords());
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<ArticleArchiveVo> articleArchive(PageQuery pageQuery) {
        LambdaQueryWrapper<ArticleInformation> wrapper = new LambdaQueryWrapper<ArticleInformation>()
                .eq(ArticleInformation::getState, StatusEnums.NORMAL.getCode())
                .eq(ArticleInformation::getAuditState, ArticleAuditStateEnums.APPROV.getCode())
                .eq(ArticleInformation::getPlatformType, 1)
                .orderByDesc(ArticleInformation::getCreateTime);
        String tripartiteUuid = LoginHelper.getTripartiteUuid();
        if (tripartiteUuid != null) {
            wrapper.eq(ArticleInformation::getUserId, tripartiteUuid);
        }
        Page<ArticleInformation> articleInformationPage = baseMapper.selectPage(pageQuery.build(), wrapper);
        List<ArticleInformation> records = articleInformationPage.getRecords();
        TableDataInfo tableDataInfo = new TableDataInfo();
        if (CollectionUtil.isEmpty(records)) return tableDataInfo;

        Map<String, List<ArticleInformation>> collect = records.stream().collect(Collectors.groupingBy(
                item -> DateUtil.format(item.getCreateTime(), "yyyy")));
        //对key 按时间降序排序
        List<Map.Entry<String, List<ArticleInformation>>> entryList = new ArrayList<>(collect.entrySet());
        // 定义时间格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
        entryList.sort((o1, o2) -> {
            Year year1 = Year.parse(o1.getKey(), formatter);
            Year year2 = Year.parse(o2.getKey(), formatter);
            return year2.compareTo(year1);
        });
        List<ArticleArchiveVo> list = new ArrayList<>();
        for (Map.Entry<String, List<ArticleInformation>> entry : entryList) {
            ArticleArchiveVo articleArchiveVo = new ArticleArchiveVo(entry.getKey(), entry.getValue());
            list.add(articleArchiveVo);
        }
        tableDataInfo.setTotal(articleInformationPage.getTotal());
        tableDataInfo.setCode(HttpStatus.HTTP_OK);
        tableDataInfo.setRows(list);
        return tableDataInfo;
    }

    @Override
    public ArticleInformationVo details(Long id) {
        ArticleInformationVo details = baseMapper.details(id);
        if (details == null) throw new ServiceException("文章不存在");
        String uuid1 = LoginHelper.getTripartiteUuid();
        UserFollow userFollow = userFollowMapper.selectOne(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getTargetId, details.getUserId())
                .eq(UserFollow::getType, UserFollowTypeEnums.b_user_follow.getCode())
                .eq(UserFollow::getUid, uuid1));
        if (ObjectUtils.isNotEmpty(userFollow) && userFollow.getTargetId().equals(details.getUserId())) {
            details.setIsFollow(true);
        }

        if (StringUtils.isNotBlank(details.getLabelId())) {
            List<LabelInfo> labelInfos = labelInfoMapper.selectList();
            Map<String, String> map = labelInfos.stream().collect(Collectors.toMap(LabelInfo::getIdStr, LabelInfo::getLabelName));
            List<String> labelIds = Arrays.asList(details.getLabelId().split(","));
            List<LabelInfo> list = new ArrayList<>();
            details.setLabelLongList(labelIds.stream().map(item -> Long.valueOf(item)).toList());
            labelIds.forEach(item -> {
                if (map.get(item) != null) {
                    LabelInfo labelInfo = new LabelInfo();
                    labelInfo.setId(Long.valueOf(item));
                    labelInfo.setLabelName(map.get(item));
                    list.add(labelInfo);
                }
            });
            details.setLabelList(list);
        }
        //获取专栏数据
        details.setGroupingName(labelGroupingInfoMapper.selectNameById(details.getGroupingId()));
        // 获取实时统计数据
        Long articleId = details.getId();
        details.setLikeTimes((long) articleCountQueryHelper.likeCount(articleId));
        Map<Long, Integer> commentCounts = articleCountQueryHelper.commentCount(List.of(articleId));
        details.setCommentTimes(commentCounts.getOrDefault(articleId, 0).longValue());
        Map<Long, Integer> collectionCounts = articleCountQueryHelper.collectionCount(List.of(articleId));
        details.setCollectionTimes(collectionCounts.getOrDefault(articleId, 0));

        // 获取作者统计（文章数、粉丝数、获赞数）
        String authorUuid = details.getUserId();
        Map<String, Integer> articleCounts = userCountQueryHelper.articleCount(List.of(authorUuid));
        details.setArticleCount(articleCounts.getOrDefault(authorUuid, 0));
        Map<String, Integer> fansCounts = userCountQueryHelper.fansCount(List.of(authorUuid));
        details.setFansFollowCount(fansCounts.getOrDefault(authorUuid, 0));
        Map<String, Integer> fabulousCounts = userCountQueryHelper.fabulousCount(List.of(authorUuid));
        details.setFabulousCount(fabulousCounts.getOrDefault(authorUuid, 0));

        String uuid = LoginHelper.getTripartiteUuid();
        // 获取当前用户点赞状态
        if (uuid != null) {
            String redisKey = RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey();
            Object likeTimestamp = RedisUtils.getCacheMapValue(redisKey, articleId + ":" + uuid);
            if (likeTimestamp != null) {
                long ts = Long.parseLong(likeTimestamp.toString());
                details.setIsFabulous((System.currentTimeMillis() - ts) < 7L * 24 * 60 * 60 * 1000);
            }
        }
        if (ObjectUtils.isEmpty(uuid)) return details;
        //获取当前用户收藏状态
        CollectionRecord collectionRecord = collectionRecordMapper.selectOne(new LambdaQueryWrapper<CollectionRecord>()
                .eq(CollectionRecord::getType, CollectionTypeEnums.ARTICLE_TYPE.getCode())
                .eq(CollectionRecord::getUid, uuid)
                .eq(CollectionRecord::getTargetId, details.getId()));
        if (collectionRecord == null) {
            details.setIsCollection(false);
        } else {
            details.setIsCollection(true);
            details.setCollectionRecordId(collectionRecord.getId());
            details.setCollectionId(collectionRecord.getCollectionId());
        }
        return details;
    }

    @Override
    public List<ArticleInformationVo> relatedList(ArticleInformationBo bo, PageQuery pageQuery) {
        bo.setArticleTitle(EscapeUtil.escapeChar(bo.getArticleTitle()));
        List<ArticleInformationVo> list = baseMapper.relatedList(bo, pageQuery);
        enrichWithRealTimeCounts(list);
        return list;
    }


    @Override
    public ArticleInformationVo getArticle(Long id) {
        ArticleInformationVo articleInformationVo = baseMapper.selectVoByIds(id);
        if (StringUtils.isNotBlank(articleInformationVo.getLabelId())) {
            List<LabelInfo> labelInfos = labelInfoMapper.selectList();
            Map<String, Long> map = labelInfos.stream().collect(Collectors.toMap(LabelInfo::getIdStr, LabelInfo::getId));
            List<String> strings = Arrays.asList(articleInformationVo.getLabelId().split(","));
            List<Long> list = new ArrayList<>();
            strings.forEach(item -> {
                if (map.get(item) != null) {
                    list.add(map.get(item));
                }
            });
            articleInformationVo.setLabelLongList(list);
        }
        return articleInformationVo;
    }

    @Override
    public ArticleInformationVo basicInfo(Long id) {
        return baseMapper.basicInfo(id);
    }

    @Override
    public Page<ArticleInformationVo> getArticleInfo(ArticleInformationBo bo, PageQuery pageQuery) {
        bo.setArticleTitle(EscapeUtil.escapeChar(bo.getArticleTitle()));
        Page<ArticleInformationVo> articleInfo = baseMapper.getArticleInfo(bo, pageQuery.build());
        enrichWithRealTimeCounts(articleInfo.getRecords());
        return articleInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        return baseMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleInformationVo saveDraft(ArticleInformationTwoBo bo) {
        //添加草稿
        bo.setAuditState(ArticleAuditStateEnums.DRAFT.getCode());
        ArticleInformationBo articleInformationBo = BeanUtil.toBean(bo, ArticleInformationBo.class);
        ArticleInformation info = BeanUtil.toBean(bo, ArticleInformation.class);
        ArticleInformationVo vo = new ArticleInformationVo();
        if (info.getId() == null) return this.insertByBo(articleInformationBo);
        String uuid = LoginHelper.getTripartiteUuid();
        info.setUpdateId(uuid);
        info.setUpdateTime(new Date());
        baseMapper.updateById(info);
        vo.setId(info.getId());
        return vo;
    }

    @Override
    public List<ArticleInformationVo> getArticleInfoList(ArticleInformationBo bo, PageQuery pageQuery) {
        List<ArticleInformationVo> list = baseMapper.selectTime(bo);
        enrichWithRealTimeCounts(list);
        return list;
    }

    @Override
    public List<ArticleInformationVo> selected() {
        List<ArticleInformationVo> list = baseMapper.selected();
        enrichWithRealTimeCounts(list);
        return list;
    }

    /**
     * 记录文章亲密度
     *
     * @param uuid
     * @param labelId
     */
    public void recordArticleIntimacy(String uuid, String labelId, Double score) {
        if (StringUtils.isEmpty(labelId) || StringUtils.isEmpty(uuid)) return;
        String[] split = labelId.split(",");
        for (String label : split) {
            if (com.baomidou.mybatisplus.core.toolkit.StringUtils.isNotEmpty(label)) {
                RedisUtils.zAddScore(String.format(RedisBusinessKeyEnums.ARTICLE_INTIMACY.getKey(), uuid), label, score);
            }
        }
    }

    @Override
    public Boolean addArticleBrowse(Long id, String label, HttpServletRequest request) {
        String ip = AddressUtils.gainIp(request);
        if (ip == null) return true;
        String key = String.format(RedisBusinessKeyEnums.ARTICLE_GLANCE_OVER.getKey(), id, ip);
        if (RedisUtils.hasKey(key)) return true;
        RedisUtils.setCacheObject(key, key, 12, TimeUnit.HOURS);
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null || StringUtils.isEmpty(label)) return true;
        //记录文章亲密度
        recordArticleIntimacy(uuid, label, 1D);
        return baseMapper.updateAdd(id, ArticleUpdateTypeEnums.BROWSE_COUNT.getCode()) > 0;
    }

    @Override
    public List<CensusVo> timeArticleCensus(CensusEntity bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        List<CensusVo> list = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(bo.getMonth())) {
            list = baseMapper.selectCensusMonth(bo);
        }
        if (ObjectUtils.isNotEmpty(bo.getYear())) {
            list = baseMapper.selectCensusYear(bo);
        }
        Map<Date, Long> collect = list.stream()
                .collect(Collectors.groupingBy(e -> DateUtils.parseDate(e.getDateTimes()), Collectors.counting()));
        List<CensusVo> list1 = new ArrayList<>();
        collect.forEach((k, v) -> {
            CensusVo censusVo = new CensusVo().setDateTimes(DateUtil.format(k, "yyyy-MM")).setCensusSum(v);
            list1.add(censusVo);
        });
        return list1;
    }

    @Override
    public Page<ArticleInformationVo> FollowArticleInfoList(SortTypeBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (ObjectUtils.isEmpty(uuid)) return null;
        bo.setUid(uuid);
        Page<ArticleInformationVo> page = baseMapper.FollowArticleInfoList(bo, pageQuery.build());
        enrichWithRealTimeCounts(page.getRecords());
        return page;
    }

    @Override
    public Page<ArticleInformationVo> LabelGArticleInfoList(SortTypeBo bo, PageQuery pageQuery) {
        Page<ArticleInformationVo> page = baseMapper.LabelGArticleInfoList(bo, pageQuery.build());
        enrichWithRealTimeCounts(page.getRecords());
        return page;
    }

    /**
     * 为文章列表补充实时统计数据（评论数、收藏数、点赞数）
     */
    private void enrichWithRealTimeCounts(List<ArticleInformationVo> articles) {
        if (CollectionUtils.isEmpty(articles)) return;
        List<Long> articleIds = articles.stream().map(ArticleInformationVo::getId).toList();
        Map<Long, Integer> commentCounts = articleCountQueryHelper.commentCount(articleIds);
        Map<Long, Integer> collectionCounts = articleCountQueryHelper.collectionCount(articleIds);
        Map<Long, Integer> likeCounts = articleCountQueryHelper.likeCount(articleIds);
        articles.forEach(article -> {
            Long id = article.getId();
            article.setCommentTimes(commentCounts.getOrDefault(id, 0).longValue());
            article.setCollectionTimes(collectionCounts.getOrDefault(id, 0));
            article.setLikeTimes(likeCounts.getOrDefault(id, 0).longValue());
        });
    }

}


