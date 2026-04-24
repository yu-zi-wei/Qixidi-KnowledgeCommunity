package com.qixidi.business.service.count;

import cn.hutool.core.collection.CollUtil;
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
        return toMap(commentMapper.selectFansCommentCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getFansCommentCount);
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
        return toMap(fabulousMapper.selectFabulousCountByUserIds(uuids),
            CountUserWebsiteVo::getUuid, CountUserWebsiteVo::getFabulousCount);
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
