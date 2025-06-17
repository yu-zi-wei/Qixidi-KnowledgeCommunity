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
import com.light.core.utils.AlgorithmUtils;
import com.light.core.utils.DateUtils;
import com.light.core.utils.StringUtils;
import com.light.core.utils.email.MailUtils;
import com.light.core.utils.ip.AddressUtils;
import com.light.core.utils.word.WordFilter;
import com.light.redission.utils.RedisUtils;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.article.ArticleInformationBo;
import com.qixidi.business.domain.bo.article.ArticleInformationTwoBo;
import com.qixidi.business.domain.bo.article.SortTypeBo;
import com.qixidi.business.domain.entity.SearchRecords;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.collection.CollectionRecord;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.entity.news.NewsSystemInfo;
import com.qixidi.business.domain.entity.special.SpecialInformation;
import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.domain.enums.CollectionType;
import com.qixidi.business.domain.enums.CountUserType;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.UserFollowType;
import com.qixidi.business.domain.enums.article.ArticleAuditStateType;
import com.qixidi.business.domain.enums.article.ArticleUpdateType;
import com.qixidi.business.domain.vo.article.ArticleArchiveVo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.collection.CollectionRecordVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.mapper.SearchRecordsMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.news.NewsSystemInfoMapper;
import com.qixidi.business.mapper.shield.ToShieldWordMapper;
import com.qixidi.business.mapper.special.SpecialInformationMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.qixidi.business.service.article.IArticleInformationService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@RequiredArgsConstructor
@Service
public class ArticleInformationServiceImpl implements IArticleInformationService {

    private final ArticleInformationMapper baseMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final LabelGroupingInfoMapper labelGroupingInfoMapper;
    private final CountUserWebsiteMapper countUserWebsiteMapper;
    private final CollectionRecordMapper collectionRecordMapper;
    private final UserFollowMapper userFollowMapper;
    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;
    private final SearchRecordsMapper searchRecordsMapper;
    private final ToShieldWordMapper toShieldWordMapper;
    private final NewsSystemInfoMapper newsSystemInfoMapper;
    private final SpecialInformationMapper specialInformationMapper;
    private final DeepSeekService deepSeekService;

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
        String uuid = LoginHelper.getTripartiteUuid();
        ArticleInformationVo vo = new ArticleInformationVo();
        add.setUserId(uuid);
        add.setCreateTime(new Date());
        add.setUpdateTime(new Date());
        baseMapper.insert(add);
        Long id = add.getId();
        vo.setId(id);
        if (bo.getAuditState().equals(4)) return vo;
        ArticleInformationVo articleInformationVo = BeanUtil.toBean(add, ArticleInformationVo.class);
        List<ArticleInformationVo> list = new ArrayList();
        list.add(articleInformationVo);
        //        计算文章权重，审核文章
        executorService.execute(() -> {
            //计算文章推荐权证
            articleWeightAlgorithms(list);
            //生成 ai总结
            aiSummary(id, add.getArticleTitle(), add.getArticleContent());
            //生成ai摘要
            if (bo.getAbstractSelect() != null && bo.getAbstractSelect()) {
                aiAbstract(id, add.getArticleTitle(), add.getArticleContent());
            }
            //文章自动审核，发送消息
            articleReview(bo.getArticleTitle(), bo.getArticleContent(), bo.getArticleAbstract(), id, uuid);
            // 重算专栏数据
            recalculationColumn(uuid);
        });

        return vo;
    }


    public void recalculationColumn(String uuid) {
        List<SpecialInformation> specialInformations = specialInformationMapper.selectList(
                new LambdaQueryWrapper<SpecialInformation>().select(SpecialInformation::getId).eq(SpecialInformation::getUid, uuid));

        if (CollectionUtil.isEmpty(specialInformations)) return;
        List<Long> specialIds = specialInformations.stream().map(SpecialInformation::getId).collect(Collectors.toList());
        List<ArticleInformation> articleInformations = baseMapper.selectList(new LambdaQueryWrapper<ArticleInformation>()
                .select(ArticleInformation::getId, ArticleInformation::getSpecialId)
                .in(ArticleInformation::getSpecialId, specialIds)
                .eq(ArticleInformation::getState, 0)
                .eq(ArticleInformation::getAuditState, 2)
                .isNotNull(ArticleInformation::getSpecialId));
        if (CollectionUtil.isEmpty(articleInformations)) return;
        Map<Long, Long> collectMap = articleInformations.stream().collect(Collectors.groupingBy(ArticleInformation::getSpecialId, Collectors.counting()));
        List<SpecialInformation> updateSpecialInformations = new ArrayList<>();
        specialInformations.forEach(item -> {
            Long sum = collectMap.get(item.getId());
            if (sum != null) {
                SpecialInformation specialInformation = new SpecialInformation();
                specialInformation.setId(item.getId());
                specialInformation.setIncludedCount(Integer.valueOf(sum.toString()));
                updateSpecialInformations.add(specialInformation);
            }
        });
        specialInformationMapper.updateBatchById(updateSpecialInformations);
    }

    public void articleReview(String Title, String Content, String Abstract, Long id, String uuid) {
        if (id == null) return;
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
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "文章自动审核失败",
                    String.format("文章名称：%s，标题触发敏感词数：%s，内容触发敏感词数：%s，摘要触发敏感词数：%s",
                            Title, wordCount, wordCount1, wordCount2));
            return;
        }
//                修改文章状态
        baseMapper.update(null, new LambdaUpdateWrapper<ArticleInformation>()
                .set(ArticleInformation::getAuditState, 2).set(ArticleInformation::getAuditTime, new Date())
                .eq(ArticleInformation::getId, id));
        //文章数量加一
        countUserWebsiteMapper.updateAdd(uuid, CountUserType.ARTICLE_COUNT.getCode());
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
    }

    public void articleWeightAlgorithms(List<ArticleInformationVo> list) {
        List<ArticleInformation> heatWeightList = list.stream().map(item -> {
            ArticleInformation articleInformation = new ArticleInformation();
            BeanUtils.copyProperties(item, articleInformation);
            Map<String, Integer> datePoor = DateUtils.getDatePoor(item.getCreateTime(), new Date());
            Integer day = datePoor.get("day");
            double heatWeight = (articleInformation.getLikeTimes() == null ? 0 : articleInformation.getLikeTimes())
                    + (articleInformation.getCommentTimes() == null ? 0 : articleInformation.getCommentTimes() * 2)
                    + (articleInformation.getCollectionTimes() == null ? 0 : articleInformation.getCollectionTimes() * 2)
                    + (articleInformation.getNumberTimes() == null ? 0 : articleInformation.getNumberTimes())
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
        Integer integer = judgmentPlusOne(bo);
        ArticleInformation update = BeanUtil.toBean(bo, ArticleInformation.class);
        String uuid = LoginHelper.getTripartiteUuid();
        update.setUpdateId(uuid);
        update.setUpdateTime(new Date());
        if (integer > 0) {
            ArticleInformationVo articleInformationVo = BeanUtil.toBean(update, ArticleInformationVo.class);
            List<ArticleInformationVo> list = new ArrayList();
            list.add(articleInformationVo);
            //        计算文章权重，审核文章
            executorService.execute(() -> {
                //计算文章权重
                articleWeightAlgorithms(list);
                //更新文章
                articleReview(bo.getArticleTitle(), bo.getArticleContent(), bo.getArticleAbstract(), update.getId(), uuid);
            });
        } else if (integer < 0) {
            countUserWebsiteMapper.updateDelete(uuid, CountUserType.ARTICLE_COUNT.getCode());
        }
        if (baseMapper.updateById(update) > 0) {
            ArticleInformationVo articleInformationVo = new ArticleInformationVo();
            articleInformationVo.setId(bo.getId());
            executorService.execute(() -> {
                //生成 ai总结
                aiSummary(update.getId(), update.getArticleTitle(), update.getArticleContent());
                if (bo.getAbstractSelect() != null && bo.getAbstractSelect()) {
                    //生成ai摘要
                    aiAbstract(update.getId(), update.getArticleTitle(), update.getArticleContent());
                }
            });
            return articleInformationVo;
        }
        //  重算专栏数据
        recalculationColumn(uuid);
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
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n 文章标题：" + articleTitle);
        stringBuffer.append("\n 文章内容：" + articleContent);
        stringBuffer.append("\n 要求：对该文章内容生成简单的文章摘要，要求纯文字返回，不要返回markdown格式，且不超过400个字符。");
        Object Summary = deepSeekService.generationContent(stringBuffer.toString());
        if (Summary != null) {
            baseMapper.update(new LambdaUpdateWrapper<ArticleInformation>()
                    .set(ArticleInformation::getArticleAbstract, Summary.toString())
                    .eq(ArticleInformation::getId, id));
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
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("\n 文章标题：" + articleTitle);
        stringBuffer.append("\n 文章内容：" + articleContent);
        stringBuffer.append("\n 要求：对该文章内容生成简单的总结，只需要总结这篇文章的大概内容，不需要详细总结。要求纯文字返回，不要返回markdown格式，且不超过400个字符。");
        Object Summary = deepSeekService.generationContent(stringBuffer.toString());
        if (Summary != null) {
            baseMapper.update(new LambdaUpdateWrapper<ArticleInformation>()
                    .set(ArticleInformation::getArticleSummary, Summary.toString())
                    .eq(ArticleInformation::getId, id));
        }
    }

    /**
     * 判断文章是否需要加一
     *
     * @param bo
     * @return 1：文章数加一，0：无变化，-1：文章数减一
     */
    public Integer judgmentPlusOne(ArticleInformationBo bo) {
        ArticleInformationVo articleVo = baseMapper.selectAuditStatus(bo.getId());
//        添加文章
        if (ObjectUtils.isEmpty(articleVo)
                && bo.getAuditState().equals(ArticleAuditStateType.APPROV.getCode())) return 1;
//        保存草稿
        if (ObjectUtils.isEmpty(articleVo)
                && bo.getAuditState().equals(ArticleAuditStateType.DRAFT.getCode())) return 0;
//        正常更新
        if (articleVo.getAuditState().equals(ArticleAuditStateType.APPROV.getCode())
                && bo.getAuditState().equals(ArticleAuditStateType.APPROV.getCode())) return 0;
//        文章变为草稿
        if (articleVo.getAuditState().equals(ArticleAuditStateType.APPROV.getCode())
                && bo.getAuditState().equals(ArticleAuditStateType.DRAFT.getCode())) return -1;
//        草稿变为文章
        if (articleVo.getAuditState().equals(ArticleAuditStateType.DRAFT.getCode())
                && bo.getAuditState().equals(ArticleAuditStateType.APPROV.getCode())) return 1;
        return 0;
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
        return TableDataInfo.build(index);
    }

    @Override
    public IPage<ArticleInformationVo> sortIndex(SortTypeBo bo, PageQuery pageQuery) {
        return baseMapper.selectTypeSort(bo, pageQuery.build());
    }

    @Override
    public IPage<ArticleInformationVo> articleList(ArticleInformationBo bo, PageQuery pageQuery) {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid != null || bo.getArticleTitle() != null) {
            executorService.execute(() -> {
                SearchRecords searchRecords = new SearchRecords();
                searchRecords.setUid(uuid);
                searchRecords.setCreateTime(new Date());
                searchRecords.setContent(bo.getArticleTitle());
                searchRecordsMapper.insert(searchRecords);
            });
        }
        return baseMapper.articleList(bo, pageQuery.build());
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
        return baseMapper.articleRecommendList(bo, result, pageQuery.build());
    }

    @Override
    public List<ArticleInformationVo> latelyArticleList(ArticleInformationBo bo, PageQuery pageQuery) {
        return baseMapper.latelyArticleList(bo, pageQuery.build());
    }

    @Override
    public TableDataInfo<ArticleArchiveVo> articleArchive(PageQuery pageQuery) {
        LambdaQueryWrapper<ArticleInformation> wrapper = new LambdaQueryWrapper<ArticleInformation>()
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
        String uuid1 = LoginHelper.getTripartiteUuid();
        UserFollow userFollow = userFollowMapper.selectOne(new QueryWrapper<UserFollow>()
                .eq("target_id", details.getUserId())
                .eq("type", UserFollowType.b_user_follow.getCode())
                .eq("uid", uuid1));
        if (ObjectUtils.isNotEmpty(userFollow) && userFollow.getTargetId().equals(details.getUserId())) {
            details.setIsFollow(true);
        }

        if (StringUtils.isNotBlank(details.getLabelId())) {
            List<LabelInfo> labelInfos = labelInfoMapper.selectList();
            Map<String, String> map = labelInfos.stream().collect(Collectors.toMap(LabelInfo::getIdStr, LabelInfo::getLabelName));
            List<String> strings = Arrays.asList(details.getLabelId().split(","));
            List<LabelInfo> list = new ArrayList<>();

            strings.forEach(item -> {
                if (map.get(item) != null) {
                    LabelInfo labelInfo = new LabelInfo();
                    labelInfo.setId(Long.valueOf(item));
                    labelInfo.setLabelName(map.get(item));
                    list.add(labelInfo);
                }
            });
            details.setLabelList(list);
        }
//        获取专栏数据
        details.setGroupingName(labelGroupingInfoMapper.selectNameById(details.getGroupingId()));
        String uuid = LoginHelper.getTripartiteUuid();
        if (ObjectUtils.isEmpty(uuid)) return details;
//        获取点赞数据
        Map<String, Object> cacheMap = RedisUtils.getCacheMap(RedisBusinessKeyEnums.TOTAL_LIKE_COUNT_KEY.getKey());
        if (CollectionUtils.isNotEmpty(cacheMap)) {
            details.setLikeTimes(cacheMap.get(details.getId().toString()) == null
                    ? details.getLikeTimes() : Long.valueOf(cacheMap.get(details.getId().toString()).toString()));
        }
        Map<String, Set<String>> FaMap = RedisUtils.getCacheMap(RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey());
        if (CollectionUtils.isNotEmpty(FaMap)) {
            details.setFabulousUserSet(FaMap.get(details.getId().toString()));
        }
//        获取收藏数据
        CollectionRecordVo collectionRecordVo = collectionRecordMapper.selectVoOne(new QueryWrapper<CollectionRecord>()
                .eq("uid", uuid)
                .eq("type", CollectionType.ARTICLE_TYPE.getCode())
                .eq("target_id", details.getId()));
        if (ObjectUtils.isEmpty(collectionRecordVo)) {
            details.setIsCollection(false);
        } else {
            details.setIsCollection(true);
            details.setCollectionRId(collectionRecordVo.getId());
        }
        return details;
    }

    @Override
    public List<ArticleInformationVo> relatedList(ArticleInformationBo bo, PageQuery pageQuery) {
        return baseMapper.relatedList(bo, pageQuery);
    }


    @Override
    public ArticleInformationVo getArtticle(Long id) {
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
        Page<ArticleInformationVo> articleInfo = baseMapper.getArticleInfo(bo, pageQuery.build());
        return articleInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(Long id) {
        ArticleInformationVo articleInformationVo = baseMapper.basicInfo(id);
        if (articleInformationVo.getAuditState().equals(ArticleAuditStateType.APPROV.getCode())) {
            countUserWebsiteMapper.updateDelete(articleInformationVo.getUserId(), CountUserType.ARTICLE_COUNT.getCode());
        }
        return baseMapper.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ArticleInformationVo saveDraft(ArticleInformationTwoBo bo) {
        ArticleInformationBo articleInformationBo = BeanUtil.toBean(bo, ArticleInformationBo.class);
        ArticleInformation info = BeanUtil.toBean(bo, ArticleInformation.class);
        ArticleInformationVo vo = new ArticleInformationVo();
        //添加草稿
        if (info.getId() == null) return this.insertByBo(articleInformationBo);
        Integer integer = judgmentPlusOne(articleInformationBo);
        String uuid = LoginHelper.getTripartiteUuid();
        info.setUpdateId(uuid);
        info.setUpdateTime(new Date());
        baseMapper.updateById(info);
        LoginHelper.getTripartiteUuid();
        if (integer > 0) {
            //文章数加一
            countUserWebsiteMapper.updateAdd(uuid, CountUserType.ARTICLE_COUNT.getCode());
        } else if (integer < 0) {
            //文章数减一
            countUserWebsiteMapper.updateDelete(uuid, CountUserType.ARTICLE_COUNT.getCode());
        }
        vo.setId(info.getId());
        return vo;
    }

    @Override
    public List<ArticleInformationVo> getArticleInfoList(ArticleInformationBo bo, PageQuery pageQuery) {
        return baseMapper.selectTime(bo);
    }

    @Override
    public List<ArticleInformationVo> selected() {
        return baseMapper.selected();
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
        RedisUtils.setCacheObject(key, key, 30, TimeUnit.MINUTES);
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null || StringUtils.isEmpty(label)) return true;
        //记录文章亲密度
        recordArticleIntimacy(uuid, label, 1D);
        return baseMapper.updateAdd(id, ArticleUpdateType.BROWSE_COUNT.getCode()) > 0;
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
        return baseMapper.FollowArticleInfoList(bo, pageQuery.build());
    }

    @Override
    public Page<ArticleInformationVo> LabelGArticleInfoList(SortTypeBo bo, PageQuery pageQuery) {
        return baseMapper.LabelGArticleInfoList(bo, pageQuery.build());
    }

}


