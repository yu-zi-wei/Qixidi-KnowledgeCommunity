package com.qixidi.business.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.light.core.utils.DateUtils;
import com.light.redission.utils.RedisUtils;
import com.qixidi.business.domain.enums.CollectionTypeEnums;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.qixidi.business.domain.enums.UserFollowTypeEnums;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.collection.CollectionRecord;
import com.qixidi.business.domain.entity.fabulous.FabulousRecord;
import com.qixidi.business.domain.entity.user.BrowsingHistory;
import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
import com.qixidi.business.mapper.user.BrowsingHistoryMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 重算文章标签亲密度（Redis zset ARTICLE_INTIMACY:GROUP:{uid}，精选推荐的数据源）
 * <p>
 * 增量路径（浏览+1/点赞+2/收藏+3/关注+5）只累加不衰减，老兴趣永远霸榜且无法自愈脏分；
 * 本任务按 DB 全量行为重建每个用户的 zset：行为分 × 时间衰减（30 天半衰期），关注为持续兴趣不衰减。
 * 覆盖式写回（先删旧 key），重算后与增量分同单位（衰减权重仅作用于历史行为）。
 *
 * @author zi-wei
 */
@Slf4j
@Service
public class ArticleIntimacyTask {

    /** 行为窗口：只统计近 90 天的浏览/点赞/收藏（90 天外衰减至 1/8 以下，不再计入） */
    private static final long BEHAVIOR_WINDOW_DAYS = 90L;
    /** 半衰期：30 天前的行为权重减半 */
    private static final double HALF_LIFE_DAYS = 30D;
    private static final double BROWSE_SCORE = 1D;
    private static final double FABULOUS_SCORE = 2D;
    private static final double COLLECTION_SCORE = 3D;
    /** 关注是当下明确的兴趣声明（持续状态），不随时间衰减 */
    private static final double FOLLOW_SCORE = 5D;
    private static final long DAY_MILLIS = 86400_000L;

    @Autowired
    private BrowsingHistoryMapper browsingHistoryMapper;
    @Autowired
    private FabulousRecordMapper fabulousRecordMapper;
    @Autowired
    private CollectionRecordMapper collectionRecordMapper;
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Autowired
    private ArticleInformationMapper articleInformationMapper;
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;

    /**
     * 重算文章标签亲密度，每天 04:30（与增量分值口径一致，另叠加时间衰减）
     */
    @Scheduled(cron = "0 30 4 * * *")
    public void recalculateArticleIntimacy() {
        try {
            Date now = new Date();
            Date threshold = new Date(now.getTime() - BEHAVIOR_WINDOW_DAYS * DAY_MILLIS);

            // 1. 全量拉近窗口行为（指定列，站点量级小一次拉全量内存分组，禁止逐用户查库）
            //    浏览历史按（uid+target）去重合并，update_time 即最后浏览时间
            List<BrowsingHistory> browses = browsingHistoryMapper.selectList(new LambdaQueryWrapper<BrowsingHistory>()
                    .select(BrowsingHistory::getUid, BrowsingHistory::getTargetId, BrowsingHistory::getUpdateTime)
                    .eq(BrowsingHistory::getTargetType, 1)
                    .ge(BrowsingHistory::getUpdateTime, threshold));
            List<FabulousRecord> fabulous = fabulousRecordMapper.selectList(new LambdaQueryWrapper<FabulousRecord>()
                    .select(FabulousRecord::getUid, FabulousRecord::getTypeId, FabulousRecord::getCreateTime)
                    .eq(FabulousRecord::getType, 1)
                    .eq(FabulousRecord::getState, 0)
                    .ge(FabulousRecord::getCreateTime, threshold));
            List<CollectionRecord> collections = collectionRecordMapper.selectList(new LambdaQueryWrapper<CollectionRecord>()
                    .select(CollectionRecord::getUid, CollectionRecord::getTargetId, CollectionRecord::getCreateTime)
                    .eq(CollectionRecord::getType, CollectionTypeEnums.ARTICLE_TYPE.getCode())
                    .eq(CollectionRecord::getState, 0)
                    .ge(CollectionRecord::getCreateTime, threshold));
            //    关注全量（不衰减，无窗口）
            List<UserFollow> labelFollows = userFollowMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                    .select(UserFollow::getUid, UserFollow::getTargetId)
                    .eq(UserFollow::getType, UserFollowTypeEnums.LABEL_FOLLOW.getCode())
                    .eq(UserFollow::getState, 0));

            // 2. 涉及文章 → 标签串（label_id 为逗号分隔多标签；selectBatchIds 空集合无保护，必须判空）
            Set<Long> articleIds = new HashSet<>();
            browses.forEach(b -> { if (b.getTargetId() != null) articleIds.add(b.getTargetId()); });
            fabulous.forEach(f -> { if (f.getTypeId() != null) articleIds.add(f.getTypeId()); });
            collections.forEach(c -> { if (c.getTargetId() != null) articleIds.add(c.getTargetId()); });
            Map<Long, String> articleLabels = new HashMap<>();
            if (!articleIds.isEmpty()) {
                for (ArticleInformation article : articleInformationMapper.selectBatchIds(articleIds)) {
                    articleLabels.put(article.getId(), article.getLabelId());
                }
            }

            // 3. 内存组装：uid → (标签 → 累计分)
            Map<String, Map<String, Double>> userScores = new HashMap<>();
            for (BrowsingHistory b : browses) {
                accumulate(userScores, b.getUid(), articleLabels.get(b.getTargetId()), decay(BROWSE_SCORE, b.getUpdateTime(), now));
            }
            for (FabulousRecord f : fabulous) {
                accumulate(userScores, f.getUid(), articleLabels.get(f.getTypeId()), decay(FABULOUS_SCORE, f.getCreateTime(), now));
            }
            for (CollectionRecord c : collections) {
                accumulate(userScores, c.getUid(), articleLabels.get(c.getTargetId()), decay(COLLECTION_SCORE, c.getCreateTime(), now));
            }
            //    关注直接对标签加分（target_id 即标签 id）
            for (UserFollow follow : labelFollows) {
                accumulate(userScores, follow.getUid(), follow.getTargetId(), FOLLOW_SCORE);
            }

            // 4. 覆盖写回：先删旧 key 再批量写（重算口径含衰减，与旧增量分不可比，不能叠加）
            for (Map.Entry<String, Map<String, Double>> entry : userScores.entrySet()) {
                String key = String.format(RedisBusinessKeyEnums.ARTICLE_INTIMACY.getKey(), entry.getKey());
                RedisUtils.deleteObject(key);
                RedisUtils.zAddScoreBatch(key, entry.getValue());
            }

            systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.RECALCULATE_ARTICLE_INTIMACY.getCode());
            log.info("重算文章标签亲密度完成：用户 {} 人，浏览 {} / 点赞 {} / 收藏 {} / 标签关注 {}",
                    userScores.size(), browses.size(), fabulous.size(), collections.size(), labelFollows.size());
        } catch (Exception e) {
            log.error("重算文章标签亲密度任务异常：时间：{}", DateUtils.getTime());
            e.printStackTrace();
        }
    }

    /**
     * 时间衰减：weight = base × 0.5^(距今天数 / 半衰期)
     */
    private double decay(double base, Date behaviorTime, Date now) {
        if (behaviorTime == null) return 0D;
        double days = (now.getTime() - behaviorTime.getTime()) / (double) DAY_MILLIS;
        return base * Math.pow(0.5, days / HALF_LIFE_DAYS);
    }

    /**
     * 累加行为分到用户-标签维度（labelId 为逗号分隔多标签串，与增量路径 recordArticleIntimacy 同拆分口径）
     */
    private void accumulate(Map<String, Map<String, Double>> userScores, String uid, String labelId, Double score) {
        if (StringUtils.isBlank(uid) || StringUtils.isBlank(labelId) || score == null || score <= 0) return;
        Map<String, Double> scores = userScores.computeIfAbsent(uid, k -> new HashMap<>());
        for (String label : labelId.split(",")) {
            if (StringUtils.isNotBlank(label)) {
                scores.merge(label.trim(), score, Double::sum);
            }
        }
    }
}
