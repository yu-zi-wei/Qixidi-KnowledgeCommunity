package com.qixidi.business.service.count;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qixidi.business.domain.entity.TimeNotes;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.dictum.DictumInfo;
import com.qixidi.business.domain.entity.fabulous.FabulousRecord;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.mapper.TimeNotesMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.comment.ArticleCommentMapper;
import com.qixidi.business.mapper.dictum.DictumAlbumMapper;
import com.qixidi.business.mapper.dictum.DictumInfoMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
import com.qixidi.business.mapper.special.SpecialInformationMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserCountQueryHelper {

    private final ArticleInformationMapper articleMapper;
    private final UserFollowMapper followMapper;
    private final ArticleCommentMapper commentMapper;
    private final CollectionRecordMapper collectionMapper;
    private final SpecialInformationMapper specialMapper;
    private final DictumAlbumMapper albumMapper;
    private final DictumInfoMapper dictumMapper;
    private final TimeNotesMapper timeNotesMapper;
    private final FabulousRecordMapper fabulousMapper;

    public Map<String, Integer> articleCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(articleMapper.selectArticleCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getArticleCount);
    }

    public Map<String, Integer> followCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(followMapper.selectFollowCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getFollowCount);
    }

    public Map<String, Integer> fansCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(followMapper.selectFansCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getFansFollowCount);
    }

    public Map<String, Integer> commentCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(commentMapper.selectCommentCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getCommentCount);
    }

    public Map<String, Integer> fansCommentCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        // 原 join 文章表语义：按文章作者统计其文章的有效评论数；两步单表（查作者文章→按文章数评论）+ 内存归并
        Map<Long, String> articleOwner = articleOwnerMap(uuids);
        if (articleOwner.isEmpty()) return Collections.emptyMap();
        Map<String, Integer> result = new HashMap<>();
        commentMapper.selectCommentCountByArticleIds(articleOwner.keySet())
            .forEach(c -> result.merge(articleOwner.get(c.getArticleId()), c.getCount(), Integer::sum));
        return result;
    }

    public Map<String, Integer> collectionCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(collectionMapper.selectCollectionCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getCollectionCount);
    }

    public Map<String, Integer> specialCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(specialMapper.selectSpecialCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getSpecialColumnCount);
    }

    public Map<String, Integer> albumCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(albumMapper.selectAlbumCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getAlbumCount);
    }

    public Map<String, Integer> dictumCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(dictumMapper.selectDictumCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getDictumCount);
    }

    public Map<String, Integer> timeNotesCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(timeNotesMapper.selectTimeNotesCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getTimeNotesCount);
    }

    public Map<String, Integer> fabulousCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        // 获赞 = 文章（type=1）+ 小记（type=2）+ 随笔（type=5）的点赞按各自内容作者归并；两步单表 + 内存归并
        Map<String, Integer> result = new HashMap<>();
        Map<Long, String> articleOwner = articleOwnerMap(uuids);
        mergeFabulousByOwner(result, 1, articleOwner);
        Map<Long, String> timeNotesOwner = timeNotesMapper.selectList(new LambdaQueryWrapper<TimeNotes>()
                        .select(TimeNotes::getId, TimeNotes::getUid)
                        .in(TimeNotes::getUid, uuids)).stream()
                .collect(Collectors.toMap(TimeNotes::getId, TimeNotes::getUid, (a, b) -> a));
        mergeFabulousByOwner(result, 2, timeNotesOwner);
        Map<Long, String> dictumOwner = dictumMapper.selectList(new LambdaQueryWrapper<DictumInfo>()
                        .select(DictumInfo::getId, DictumInfo::getUid)
                        .in(DictumInfo::getUid, uuids)).stream()
                .collect(Collectors.toMap(DictumInfo::getId, DictumInfo::getUid, (a, b) -> a));
        mergeFabulousByOwner(result, 5, dictumOwner);
        return result;
    }

    /**
     * 按内容 owner 归并某类型的获赞数
     */
    private void mergeFabulousByOwner(Map<String, Integer> result, int fabulousType, Map<Long, String> ownerMap) {
        if (ownerMap.isEmpty()) return;
        fabulousMapper.selectList(new LambdaQueryWrapper<FabulousRecord>()
                .select(FabulousRecord::getTypeId)
                .eq(FabulousRecord::getType, fabulousType)
                .in(FabulousRecord::getTypeId, ownerMap.keySet()))
            .forEach(f -> result.merge(ownerMap.get(f.getTypeId()), 1, Integer::sum));
    }

    /**
     * 文章id→作者uuid（原 join 文章表按作者聚合的等价前置查询）
     */
    private Map<Long, String> articleOwnerMap(Collection<String> uuids) {
        return articleMapper.selectList(new LambdaQueryWrapper<ArticleInformation>()
                .select(ArticleInformation::getId, ArticleInformation::getUserId)
                .in(ArticleInformation::getUserId, uuids))
            .stream()
            .collect(Collectors.toMap(ArticleInformation::getId, ArticleInformation::getUserId, (a, b) -> a));
    }

    public Map<String, Integer> viewCount(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        return toMap(articleMapper.selectViewCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getViewCount);
    }

    public Map<String, CountUserWebsiteVo> allCounts(Collection<String> uuids) {
        if (CollUtil.isEmpty(uuids)) return Collections.emptyMap();
        Map<String, CountUserWebsiteVo> result = new HashMap<>();
        uuids.forEach(u -> result.put(u, new CountUserWebsiteVo().setUuid(u)));

        merge(result, articleCount(uuids), CountUserWebsiteVo::setArticleCount);
        merge(result, followCount(uuids), CountUserWebsiteVo::setFollowCount);
        merge(result, fansCount(uuids), CountUserWebsiteVo::setFansFollowCount);
        merge(result, commentCount(uuids), CountUserWebsiteVo::setCommentCount);
        merge(result, fansCommentCount(uuids), CountUserWebsiteVo::setFansCommentCount);
        merge(result, collectionCount(uuids), CountUserWebsiteVo::setCollectionCount);
        merge(result, specialCount(uuids), CountUserWebsiteVo::setSpecialColumnCount);
        merge(result, albumCount(uuids), CountUserWebsiteVo::setAlbumCount);
        merge(result, dictumCount(uuids), CountUserWebsiteVo::setDictumCount);
        merge(result, timeNotesCount(uuids), CountUserWebsiteVo::setTimeNotesCount);
        // fabulousCount() 算的是「文章获赞数」（fans 语义），归位到被点赞数字段；
        // fabulousCount（用户发出的点赞总数）暂无统计与消费方
        merge(result, fabulousCount(uuids), CountUserWebsiteVo::setFansFabulousCount);
        merge(result, viewCount(uuids), CountUserWebsiteVo::setViewCount);
        return result;
    }

    private void merge(Map<String, CountUserWebsiteVo> map, Map<String, Integer> counts,
                       BiConsumer<CountUserWebsiteVo, Integer> setter) {
        counts.forEach((uuid, val) -> {
            CountUserWebsiteVo vo = map.get(uuid);
            if (vo != null) setter.accept(vo, val);
        });
    }

    private Map<String, Integer> toMap(List<CountUserWebsiteVo> list,
                                       Function<CountUserWebsiteVo, String> keyFn,
                                       Function<CountUserWebsiteVo, Integer> valFn) {
        if (CollUtil.isEmpty(list)) return Collections.emptyMap();
        return list.stream().collect(Collectors.toMap(keyFn, valFn));
    }
}
