package com.qixidi.business.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.light.core.constant.SystemConstant;
import com.light.core.utils.DateUtils;
import com.light.core.utils.email.MailUtils;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.dictum.DictumAlbum;
import com.qixidi.business.domain.entity.dictum.DictumGroup;
import com.qixidi.business.domain.entity.dictum.DictumInfo;
import com.qixidi.business.domain.entity.stat.StatDataInfo;
import com.qixidi.common.domain.enums.StatusEnums;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.qixidi.business.domain.enums.article.ArticleAuditStateEnums;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.TimeNotesMapper;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.dictum.DictumAlbumMapper;
import com.qixidi.business.mapper.dictum.DictumGroupMapper;
import com.qixidi.business.mapper.dictum.DictumInfoMapper;
import com.qixidi.business.mapper.special.SpecialInformationMapper;
import com.qixidi.business.mapper.stat.StatDataInfoMapper;
import com.qixidi.business.mapper.stat.StatTheDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 网站后台统计定时任务
 */
@Slf4j
@Service
public class StatTask {

    @Autowired
    private StatDataInfoMapper statDataInfoMapper;//统计数据(日)
    @Autowired
    private StatTheDataMapper statTheDataMapper;//统计数据(月)
    @Autowired
    private ArticleInformationMapper articleInformationMapper;//文章
    @Autowired
    private SpecialInformationMapper specialInformationMapper;//专栏
    @Autowired
    private TripartiteUserMapper tripartiteUserMapper;//用户
    @Autowired
    private DictumGroupMapper dictumGroupMapper;//名言分类
    @Autowired
    private DictumAlbumMapper dictumAlbumMapper;//专辑
    @Autowired
    private DictumInfoMapper dictumInfoMapper;//名言
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;
    @Autowired
    private TimeNotesMapper timeNotesMapper;

    /**
     * 更新网站每日数据
     * 每天23点执行
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void StatInfoUpdate() {
        try {
            Long articleCount = articleInformationMapper.selectCount(new LambdaQueryWrapper<ArticleInformation>()
                    .eq(ArticleInformation::getAuditState, ArticleAuditStateEnums.APPROV.getCode())
                    .eq(ArticleInformation::getState, StatusEnums.NORMAL.getCode()));
            Long specialCount = specialInformationMapper.selectCount(null);
            Long userCount = tripartiteUserMapper.selectCount(new LambdaQueryWrapper<TripartiteUser>().eq(TripartiteUser::getState, StatusEnums.NORMAL.getCode()));
            Long dictumGroupCount = dictumGroupMapper.selectCount(new LambdaQueryWrapper<DictumGroup>().eq(DictumGroup::getState, StatusEnums.NORMAL.getCode()));
            Long dictumAlbumCount = dictumAlbumMapper.selectCount(new LambdaQueryWrapper<DictumAlbum>().eq(DictumAlbum::getState, StatusEnums.NORMAL.getCode()));
            Long dictumInfoCount = dictumInfoMapper.selectCount(new LambdaQueryWrapper<DictumInfo>().eq(DictumInfo::getState, StatusEnums.NORMAL.getCode()));
            Long timeNotesCount = timeNotesMapper.selectCount(null);
            StatDataInfo statDataInfo = new StatDataInfo()
                    .setArticleCount(articleCount)
                    .setUserCount(userCount)
                    .setSpecialCount(specialCount)
                    .setStatTime(DateUtils.getDate())
                    .setDictumGroupCount(dictumGroupCount)
                    .setDictumAlbumCount(dictumAlbumCount)
                    .setTimeNotesCount(timeNotesCount)
                    .setDictumCount(dictumInfoCount);
            statDataInfoMapper.insertUpdate(statDataInfo);
            systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.UPDATE_WEBSITE_DAILY_DATA.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "网站数据更新（StatInfoUpdate）任务异常", e.getMessage());
            log.error("网站数据更新异常：时间：{}", DateUtils.getTime());
        }
    }

    /**
     * 更新网站每月数据
     * 每天0点执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void StatTheInfoUpdate() {
        try {
            StatDataInfo statDataInfo = statDataInfoMapper.selectLists(DateUtils.getThe());
            if (statDataInfo == null) return;
            statTheDataMapper.insertLists(statDataInfo);
            systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.UPDATE_WEBSITE_MONTHLY_DATA.getCode());
        } catch (Exception e) {
            e.printStackTrace();
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "网站数据更新（StatTheInfoUpdate）任务异常", e.getMessage());
            log.error("网站每月数据更新异常：时间：{}", DateUtils.getTime());
        }
    }
}
