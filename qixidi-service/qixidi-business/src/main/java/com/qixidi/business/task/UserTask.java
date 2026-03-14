package com.qixidi.business.task;

import cn.hutool.core.collection.CollectionUtil;
import com.light.core.constant.SystemConstant;
import com.light.core.utils.email.MailUtils;
import com.qixidi.business.domain.enums.CountUserTypeEnums;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.TimeNotesMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.comment.ArticleCommentMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.mapper.dictum.DictumAlbumMapper;
import com.qixidi.business.mapper.dictum.DictumInfoMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
import com.qixidi.business.mapper.special.SpecialInformationMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserTask {

    private final FabulousRecordMapper fabulousRecordMapper;//点赞
    private final ArticleInformationMapper articleInformationMapper;//文章
    private final ArticleCommentMapper articleCommentMapper;//评论
    private final CollectionRecordMapper collectionRecordMapper;//收藏
    private final SpecialInformationMapper specialInformationMapper;//专栏
    private final CountUserWebsiteMapper countUserWebsiteMapper;//用户网站信息统计
    private final UserFollowMapper userFollowMapper;//关注
    private final DictumAlbumMapper dictumAlbumMapper;//专辑
    private final DictumInfoMapper dictumInfoMapper;//名言
    private final TimeNotesMapper timeNotesMapper;//小记
    private final SystemTaskConfigMapper systemTaskConfigMapper;


    /**
     * 用户网站数据同步（b_count_user_website）
     * 每小时同步一次
     *
     * @throws Exception
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void useWebsiteSync() {
        try {
//        查询用户文章总数
            List<CountUserWebsiteVo> articleTask = articleInformationMapper.selectUserArticleTask();
            if (CollectionUtil.isNotEmpty(articleTask)) {
                Integer articleTaskSum = countUserWebsiteMapper.updateList(articleTask, CountUserTypeEnums.ARTICLE_COUNT.getCode());
            }
//        查询用户评论总数

//         查询关注总数
            List<CountUserWebsiteVo> followTask = userFollowMapper.selectFollowTask();
            if (CollectionUtil.isNotEmpty(followTask)) {
                Integer followTaskSum = countUserWebsiteMapper.updateList(followTask, CountUserTypeEnums.FOLLOW_COUNT.getCode());
            }

//         查询专栏总数
            List<CountUserWebsiteVo> specialTask = specialInformationMapper.selectSpecialTask();
            if (CollectionUtil.isNotEmpty(specialTask)) {
                Integer specialTaskSum = countUserWebsiteMapper.updateList(specialTask, CountUserTypeEnums.SPECIAL_COLUMN_COUNT.getCode());
            }
//            用户收藏夹总数
            List<CountUserWebsiteVo> collectionTask = collectionRecordMapper.selectCollectionTask();
            if (CollectionUtil.isNotEmpty(collectionTask)) {
                Integer collectionTaskSum = countUserWebsiteMapper.updateList(collectionTask, CountUserTypeEnums.COLLECTION_COUNT.getCode());
            }

            //            用户专辑总数
            List<CountUserWebsiteVo> albumTask = dictumAlbumMapper.selectAlbumTask();
            if (CollectionUtil.isNotEmpty(albumTask)) {
                Integer albumTaskSum = countUserWebsiteMapper.updateList(albumTask, CountUserTypeEnums.ALBUM_COUNT.getCode());
            }
            //            用户名言总数
            List<CountUserWebsiteVo> dictumInfo = dictumInfoMapper.selectDictumInfo();
            if (CollectionUtil.isNotEmpty(dictumInfo)) {
                Integer dictumInfoSum = countUserWebsiteMapper.updateList(dictumInfo, CountUserTypeEnums.B_DICTUM_INFO_COUNT.getCode());
            }

            //            时光小记数
            List<CountUserWebsiteVo> timeNotesTask = timeNotesMapper.selectNotesCount();
            if (CollectionUtil.isNotEmpty(timeNotesTask)) {
                Integer dictumInfoSum = countUserWebsiteMapper.updateList(timeNotesTask, CountUserTypeEnums.B_TIME_NOTES.getCode());
            }
            //  修复异常数据
            Integer extremelyInteger = countUserWebsiteMapper.updateExtremelyDate();

            systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.SYNCING_USER_SITE_DATA.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户网站数据同步  执行异常异常：{}", e.getMessage());
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "用户网站数据同步（b_count_user_website,useWebsiteSync）任务异常", e.getMessage());
        }
    }
}

