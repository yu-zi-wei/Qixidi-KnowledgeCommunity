package com.qixidi.business.service.impl.fabulous;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.redission.utils.RedisUtils;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.fabulous.FabulousRecordBo;
import com.qixidi.business.domain.bo.user.UserHomeBo;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.dictum.DictumInfo;
import com.qixidi.business.domain.entity.fabulous.FabulousRecord;
import com.qixidi.business.domain.entity.news.NewsUserRecord;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.news.NewsType;
import com.qixidi.business.domain.vo.TimeNotesInfoVo;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.domain.vo.fabulous.FabulousRecordVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.mapper.TimeNotesMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.comment.NewsUserRecordMapper;
import com.qixidi.business.mapper.dictum.DictumInfoMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
import com.qixidi.business.service.IBrowsingHistoryService;
import com.qixidi.business.service.ITripartiteUserService;
import com.qixidi.business.service.fabulous.IFabulousRecordService;
import com.qixidi.business.service.impl.article.ArticleInformationServiceImpl;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * 点赞Service业务层处理
 *
 * @author aurora
 * @date 2022-10-01
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class FabulousRecordServiceImpl implements IFabulousRecordService {

    /**
     * 点赞目标类型：与 b_fabulous_record.type 对齐（1=文章，2=小记，5=随笔）
     */
    private static final Integer TYPE_ARTICLE = 1;
    private static final Integer TYPE_TIME_NOTES = 2;
    private static final Integer TYPE_DICTUM = 5;

    /**
     * 点赞目标类型 → 足迹目标类型（b_browsing_history.target_type：1=文章，2=小记，3=随笔）
     */
    private static Integer footprintTargetType(Integer fabulousType) {
        if (Objects.equals(fabulousType, TYPE_TIME_NOTES)) return 2;
        if (Objects.equals(fabulousType, TYPE_DICTUM)) return 3;
        return 1;
    }

    private final FabulousRecordMapper baseMapper;
    private final NewsUserRecordMapper newsUserRecordMapper;
    private final ArticleInformationServiceImpl articleInformationService;
    private final ArticleInformationMapper articleInformationMapper;
    private final TimeNotesMapper timeNotesMapper;
    private final DictumInfoMapper dictumInfoMapper;
    private final ITripartiteUserService tripartiteUserService;
    private final IBrowsingHistoryService browsingHistoryService;
    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;

    /**
     * 查询点赞
     *
     * @param id 点赞主键
     * @return 点赞
     */
    @Override
    public FabulousRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询点赞列表
     *
     * @param bo 点赞
     * @return 点赞
     */
    @Override
    public TableDataInfo<FabulousRecordVo> queryPageList(FabulousRecordBo bo, PageQuery pageQuery) {
        //构造查询条件
        LambdaQueryWrapper<FabulousRecord> lqw = buildQueryWrapper(bo);
        //查询数据库
        Page<FabulousRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        //返回
        return TableDataInfo.build(result);
    }

    /**
     * 查询点赞列表
     *
     * @param bo 点赞
     * @return 点赞
     */
    @Override
    public List<FabulousRecordVo> queryList(FabulousRecordBo bo) {
        LambdaQueryWrapper<FabulousRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<FabulousRecord> buildQueryWrapper(FabulousRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<FabulousRecord> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), FabulousRecord::getUid, bo.getUid());
        lqw.eq(bo.getTargetId() != null, FabulousRecord::getTargetId, bo.getTargetId());
        lqw.eq(bo.getType() != null, FabulousRecord::getType, bo.getType());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
                FabulousRecord::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        return lqw;
    }

    /**
     * 新增点赞
     *
     * @param bo 点赞
     * @return 结果
     */
    @Override
    public Boolean insertByBo(FabulousRecordBo bo) {
        //转换
        FabulousRecord add = BeanUtil.toBean(bo, FabulousRecord.class);
        add.setCreateTime(new Date());
        add.setUid(LoginHelper.getTripartiteUuid());

        boolean flag = baseMapper.insert(add) > 0;
        return flag;
    }

    /**
     * 修改点赞
     *
     * @param bo 点赞
     * @return 结果
     */
    @Override
    public Boolean updateByBo(FabulousRecordBo bo) {
        FabulousRecord update = BeanUtil.toBean(bo, FabulousRecord.class);
        return baseMapper.updateById(update) > 0;
    }


    /**
     * 批量删除点赞
     *
     * @param ids 需要删除的点赞主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public void spotFabulous(FabulousRecordBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        log.info("点赞开始，typeId:{}，uid:{}", bo.getTypeId(), bo.getUid());

        // 0. 防重：已点赞直接返回（点赞即新增记录模式下重复插入会让计数翻倍）
        Long exists = baseMapper.selectCount(new LambdaQueryWrapper<FabulousRecord>()
                .eq(FabulousRecord::getUid, bo.getUid())
                .eq(FabulousRecord::getTypeId, bo.getTypeId())
                .eq(FabulousRecord::getType, bo.getType()));
        if (exists != null && exists > 0) {
            return;
        }

        // 1. 写 DB（点赞即新增，取消赞走物理删除）
        FabulousRecord record = new FabulousRecord();
        record.setUid(bo.getUid());
        record.setTypeId(bo.getTypeId());
        record.setTargetId(bo.getTargetId());
        record.setTargetUid(bo.getTargetUid());
        record.setType(bo.getType());
        record.setCreateTime(new Date());
        baseMapper.insert(record);

        // 2. 文章专属：Redis 点赞时间戳（热度加权）+ 标签亲密度
        if (Objects.equals(bo.getType(), TYPE_ARTICLE)) {
            String redisKey = RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey();
            RedisUtils.setCacheMapValue(redisKey, bo.getTypeId() + ":" + bo.getUid(), System.currentTimeMillis());
            articleInformationService.recordArticleIntimacy(bo.getUid(), bo.getLabelId(), 2D);
        }

        // 3. 记录用户足迹（文章/小记/随笔；行为=点赞，不覆盖评论快照）
        if (isFootprintType(bo.getType())) {
            browsingHistoryService.recordFootprint(bo.getTypeId(), bo.getTargetTitle(), bo.getTargetUid(),
                    footprintTargetType(bo.getType()), 2, null);
        }

        // 4. 通知（文章/小记各自消息类型：小记点赞跳小记详情，与文章 id 空间隔离）
        if (bo.getUid().equals(bo.getTargetUid())) return;
        Integer newsType = fabulousNewsType(bo.getType());
        executorService.execute(() -> {
            NewsUserRecord newsUserRecord = new NewsUserRecord();
            newsUserRecord.setUid(bo.getTargetUid());
            newsUserRecord.setTargetId(bo.getTargetId());
            newsUserRecord.setTargetUid(bo.getUid());
            newsUserRecord.setType(newsType);
            newsUserRecord.setContent(bo.getTargetTitle());
            newsUserRecord.setCreateTime(new Date());
            newsUserRecordMapper.insert(newsUserRecord);
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
        });
    }

    @Override
    public void cancelFabulous(FabulousRecordBo bo) {
        bo.setUid(LoginHelper.getTripartiteUuid());
        log.info("取消点赞开始，typeId:{}，uid:{}", bo.getTypeId(), bo.getUid());

        // 1. 删 DB 记录（取消赞即物理删除）
        baseMapper.delete(new LambdaQueryWrapper<FabulousRecord>()
                .eq(FabulousRecord::getUid, bo.getUid())
                .eq(FabulousRecord::getTypeId, bo.getTypeId())
                .eq(FabulousRecord::getType, bo.getType()));

        // 2. 文章专属：删 Redis 时间戳 + 回退亲密度
        if (Objects.equals(bo.getType(), TYPE_ARTICLE)) {
            String redisKey = RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey();
            RedisUtils.delCacheMapValue(redisKey, bo.getTypeId() + ":" + bo.getUid());
            articleInformationService.recordArticleIntimacy(bo.getUid(), bo.getLabelId(), -2D);
        }

        // 3. 通知（按点赞目标对应的消息类型删）
        Integer newsType = fabulousNewsType(bo.getType());
        executorService.execute(() -> {
            newsUserRecordMapper.delete(new LambdaQueryWrapper<NewsUserRecord>()
                    .eq(NewsUserRecord::getTargetUid, bo.getUid())
                    .eq(NewsUserRecord::getUid, bo.getTargetUid())
                    .eq(NewsUserRecord::getType, newsType));
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(bo.getTargetUid());
        });
    }

    /**
     * 点赞目标类型 → 消息类型（文章=2 点赞；小记=8、随笔=9 为点赞子类型，未读数合并展示进"点赞"）
     */
    private Integer fabulousNewsType(Integer targetType) {
        if (Objects.equals(targetType, TYPE_TIME_NOTES)) {
            return NewsType.TIME_NOTES_FABULOUS_NEWS.getCode();
        }
        if (Objects.equals(targetType, TYPE_DICTUM)) {
            return NewsType.DICTUM_FABULOUS_NEWS.getCode();
        }
        return NewsType.FABULOUS_NEWS.getCode();
    }

    /**
     * 是否记录足迹的点赞类型（文章/小记/随笔；评论类点赞暂无前台入口）
     */
    private boolean isFootprintType(Integer targetType) {
        return Objects.equals(targetType, TYPE_ARTICLE)
                || Objects.equals(targetType, TYPE_TIME_NOTES)
                || Objects.equals(targetType, TYPE_DICTUM);
    }

    @Override
    public TableDataInfo<ArticleInformationVo> fabulousList(UserHomeBo bo, PageQuery pageQuery) {
        // 原单条 SQL join 文章表改为两步单表（SQL 不跨业务域连表）：查点赞记录后批量查有效文章内存组装
        // type=1 限定文章：小记点赞（type=2）的 typeId 与文章 id 无关联，混入会误当文章
        List<FabulousRecord> records = baseMapper.selectList(new LambdaQueryWrapper<FabulousRecord>()
                .eq(FabulousRecord::getUid, bo.getUid())
                .eq(FabulousRecord::getType, TYPE_ARTICLE)
                .orderByDesc(FabulousRecord::getCreateTime));
        if (records.isEmpty()) {
            return TableDataInfo.build(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
        }
        List<Long> typeIds = records.stream().map(FabulousRecord::getTypeId).filter(Objects::nonNull).distinct().toList();
        Map<Long, ArticleInformation> articles = typeIds.isEmpty() ? Map.of()
                : articleInformationMapper.selectBatchIds(typeIds).stream()
                .filter(a -> a.getState() == 0 && a.getAuditState() == 2)
                .collect(Collectors.toMap(ArticleInformation::getId, a -> a, (a, b) -> a));
        // 原 DISTINCT 语义：一篇文章多条点赞记录折叠一行，展示时间取最新点赞时间（记录按时间倒序，首见即最新）
        Map<Long, FabulousRecord> latestOfArticle = new LinkedHashMap<>();
        records.forEach(r -> {
            if (articles.containsKey(r.getTypeId())) {
                latestOfArticle.putIfAbsent(r.getTypeId(), r);
            }
        });
        List<FabulousRecord> valid = new ArrayList<>(latestOfArticle.values());
        int fromIndex = (int) Math.min((long) (pageQuery.getPageNum() - 1) * pageQuery.getPageSize(), valid.size());
        int toIndex = (int) Math.min((long) fromIndex + pageQuery.getPageSize(), valid.size());
        Page<ArticleInformationVo> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize(), valid.size());
        page.setRecords(valid.subList(fromIndex, toIndex).stream().map(r -> {
            ArticleInformationVo vo = BeanUtil.toBean(articles.get(r.getTypeId()), ArticleInformationVo.class);
            vo.setCreateTime(r.getCreateTime());
            return vo;
        }).toList());
        fillAuthorDisplay(page.getRecords());
        return TableDataInfo.build(page);
    }

    @Override
    public TableDataInfo<TimeNotesInfoVo> fabulousTimeNotesList(UserHomeBo bo, PageQuery pageQuery) {
        List<FabulousRecord> records = fabulousRecordsOf(bo.getUid(), TYPE_TIME_NOTES);
        if (records.isEmpty()) {
            return TableDataInfo.build(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
        }
        // 内容已被删除的点赞记录静默过滤；展示时间取点赞时间（倒序首见即最新）
        Map<Long, FabulousRecord> latest = latestRecordByType(records);
        Map<Long, TimeNotes> notes = timeNotesMapper.selectBatchIds(latest.keySet()).stream()
                .collect(Collectors.toMap(TimeNotes::getId, n -> n, (a, b) -> a));
        List<TimeNotesInfoVo> valid = latest.entrySet().stream()
                .filter(e -> notes.containsKey(e.getKey()))
                .map(e -> {
                    TimeNotesInfoVo vo = BeanUtil.toBean(notes.get(e.getKey()), TimeNotesInfoVo.class);
                    vo.setCreateTime(e.getValue().getCreateTime());
                    return vo;
                })
                .toList();
        return buildPage(valid, pageQuery);
    }

    @Override
    public TableDataInfo<DictumInfoVo> fabulousDictumList(UserHomeBo bo, PageQuery pageQuery) {
        List<FabulousRecord> records = fabulousRecordsOf(bo.getUid(), TYPE_DICTUM);
        if (records.isEmpty()) {
            return TableDataInfo.build(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
        }
        Map<Long, FabulousRecord> latest = latestRecordByType(records);
        Map<Long, DictumInfo> dictums = dictumInfoMapper.selectBatchIds(latest.keySet()).stream()
                .filter(d -> d.getState() == null || d.getState() == 0)
                .collect(Collectors.toMap(DictumInfo::getId, d -> d, (a, b) -> a));
        List<DictumInfoVo> valid = latest.entrySet().stream()
                .filter(e -> dictums.containsKey(e.getKey()))
                .map(e -> {
                    DictumInfoVo vo = BeanUtil.toBean(dictums.get(e.getKey()), DictumInfoVo.class);
                    vo.setCreateTime(e.getValue().getCreateTime());
                    return vo;
                })
                .toList();
        fillDictumAuthor(valid);
        return buildPage(valid, pageQuery);
    }

    /**
     * 某用户某类型的全部点赞记录（按点赞时间倒序）
     */
    private List<FabulousRecord> fabulousRecordsOf(String uid, Integer type) {
        return baseMapper.selectList(new LambdaQueryWrapper<FabulousRecord>()
                .eq(FabulousRecord::getUid, uid)
                .eq(FabulousRecord::getType, type)
                .orderByDesc(FabulousRecord::getCreateTime));
    }

    /**
     * 同一内容多条点赞记录折叠一条，保留最新（记录已按时间倒序，首见即最新）
     */
    private Map<Long, FabulousRecord> latestRecordByType(List<FabulousRecord> records) {
        Map<Long, FabulousRecord> latest = new LinkedHashMap<>();
        records.forEach(r -> {
            if (r.getTypeId() != null) {
                latest.putIfAbsent(r.getTypeId(), r);
            }
        });
        return latest;
    }

    /**
     * 内存分页（记录全量在内存，内容有效性过滤后手动分页保 total 等价）
     */
    private <V> TableDataInfo<V> buildPage(List<V> valid, PageQuery pageQuery) {
        int fromIndex = (int) Math.min((long) (pageQuery.getPageNum() - 1) * pageQuery.getPageSize(), valid.size());
        int toIndex = (int) Math.min((long) fromIndex + pageQuery.getPageSize(), valid.size());
        Page<V> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize(), valid.size());
        page.setRecords(valid.subList(fromIndex, toIndex));
        return TableDataInfo.build(page);
    }

    /**
     * 随笔作者昵称展示（批量查用户域，禁循环查库）
     */
    private void fillDictumAuthor(List<DictumInfoVo> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<String> uids = records.stream()
                .map(DictumInfoVo::getUid)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        if (uids.isEmpty()) {
            return;
        }
        Map<String, TripartiteUserVo> users = tripartiteUserService.getUserByUids(uids).stream()
                .collect(Collectors.toMap(TripartiteUserVo::getUuid, u -> u, (a, b) -> a));
        records.forEach(r -> {
            TripartiteUserVo author = users.get(r.getUid());
            if (author != null) {
                r.setNickname(author.getNickname());
                r.setAvatar(author.getAvatar());
            }
        });
    }

    /**
     * 组装文章作者昵称/头像/职业（键 userId，原 SQL join b_user_main 的展示字段）
     */
    private void fillAuthorDisplay(List<ArticleInformationVo> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        List<String> uids = records.stream()
                .map(ArticleInformationVo::getUserId)
                .filter(StringUtils::isNotBlank)
                .distinct()
                .toList();
        if (uids.isEmpty()) {
            return;
        }
        Map<String, TripartiteUserVo> users = tripartiteUserService.getUserByUids(uids).stream()
                .collect(Collectors.toMap(TripartiteUserVo::getUuid, u -> u, (a, b) -> a));
        records.forEach(r -> {
            TripartiteUserVo author = users.get(r.getUserId());
            if (author != null) {
                r.setNickname(author.getNickname());
                r.setAvatar(author.getAvatar());
                r.setOccupation(author.getOccupation());
            }
        });
    }
}
