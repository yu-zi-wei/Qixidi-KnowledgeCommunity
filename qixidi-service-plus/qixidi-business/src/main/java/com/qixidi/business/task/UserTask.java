package com.qixidi.business.task;

import cn.hutool.core.collection.CollectionUtil;
import com.light.core.constant.SystemConstant;
import com.qixidi.business.domain.enums.CountUserType;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.light.core.utils.email.MailUtils;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.collection.CollectionRecordMapper;
import com.qixidi.business.mapper.comment.ArticleCommentMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.mapper.dictum.DictumAlbumMapper;
import com.qixidi.business.mapper.dictum.DictumInfoMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
import com.qixidi.business.mapper.special.SpecialInformationMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserTask {

    @Autowired
    private FabulousRecordMapper fabulousRecordMapper;//点赞
    @Autowired
    private ArticleInformationMapper articleInformationMapper;//文章
    @Autowired
    private ArticleCommentMapper articleCommentMapper;//评论
    @Autowired
    private CollectionRecordMapper collectionRecordMapper;//收藏
    @Autowired
    private SpecialInformationMapper specialInformationMapper;//专栏
    @Autowired
    private CountUserWebsiteMapper countUserWebsiteMapper;//用户网站信息统计
    @Autowired
    private UserFollowMapper userFollowMapper;//关注
    @Autowired
    private DictumAlbumMapper dictumAlbumMapper;//专辑
    @Autowired
    private DictumInfoMapper dictumInfoMapper;//名言
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;

    /**
     * 用户权重统计
     *
     * @throws Exception
     */
    public void userWeightCunt() throws Exception {

    }

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
                Integer articleTaskSum = countUserWebsiteMapper.updateList(articleTask, CountUserType.ARTICLE_COUNT.getCode());
                log.info("用户文章总数同步完成，同步数：{}", articleTaskSum);
            }
//        查询用户评论总数

//         查询关注总数
            List<CountUserWebsiteVo> followTask = userFollowMapper.selectFollowTask();
            if (CollectionUtil.isNotEmpty(followTask)) {
                Integer followTaskSum = countUserWebsiteMapper.updateList(followTask, CountUserType.FOLLOW_COUNT.getCode());
                log.info("用户关注总数同步完成，同步数：{}", followTaskSum);
            }

//         查询专栏总数
            List<CountUserWebsiteVo> specialTask = specialInformationMapper.selectSpecialTask();
            if (CollectionUtil.isNotEmpty(specialTask)) {
                Integer specialTaskSum = countUserWebsiteMapper.updateList(specialTask, CountUserType.SPECIAL_COLUMN_COUNT.getCode());
                log.info("用户专栏总数同步完成，同步数：{}", specialTaskSum);
            }
//            用户收藏夹总数
            List<CountUserWebsiteVo> collectionTask = collectionRecordMapper.selectCollectionTask();
            if (CollectionUtil.isNotEmpty(collectionTask)) {
                Integer collectionTaskSum = countUserWebsiteMapper.updateList(collectionTask, CountUserType.COLLECTION_COUNT.getCode());
                log.info("收藏夹总数同步完成，同步数：{}", collectionTaskSum);
            }

            //            用户专辑总数
            List<CountUserWebsiteVo> albumTask = dictumAlbumMapper.selectAlbumTask();
            if (CollectionUtil.isNotEmpty(albumTask)) {
                Integer albumTaskSum = countUserWebsiteMapper.updateList(albumTask, CountUserType.ALBUM_COUNT.getCode());
                log.info("专辑总数同步完成，同步数：{}", albumTaskSum);
            }
            //            用户名言总数
            List<CountUserWebsiteVo> dictumInfo = dictumInfoMapper.selectDictumInfo();
            if (CollectionUtil.isNotEmpty(dictumInfo)) {
                Integer dictumInfoSum = countUserWebsiteMapper.updateList(dictumInfo, CountUserType.b_dictum_info_COUNT.getCode());
                log.info("用户名言总数同步完成，同步数：{}", dictumInfoSum);
            }
            //  修复异常数据
            Integer extremelyInteger = countUserWebsiteMapper.updateExtremelyDate();
            log.info("异常数据完成，修复完成数：{}", extremelyInteger);
            
            systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.SYNCING_USER_SITE_DATA.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户网站数据同步  执行异常异常：{}", e.getMessage());
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "用户网站数据同步（b_count_user_website,useWebsiteSync）任务异常", e.getMessage());
        }
    }
}

