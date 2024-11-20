package com.aurora.business.task;

import com.aurora.business.domain.entity.article.ArticleInformation;
import com.aurora.business.domain.entity.stat.StatDataInfo;
import com.aurora.business.mapper.SystemTaskConfigMapper;
import com.aurora.business.mapper.article.ArticleInformationMapper;
import com.aurora.business.mapper.dictum.DictumAlbumMapper;
import com.aurora.business.mapper.dictum.DictumGroupMapper;
import com.aurora.business.mapper.dictum.DictumInfoMapper;
import com.aurora.business.mapper.special.SpecialInformationMapper;
import com.aurora.business.mapper.stat.StatDataInfoMapper;
import com.aurora.business.mapper.stat.StatTheDataMapper;
import com.aurora.common.constant.SystemConstant;
import com.aurora.common.enums.SystemTaskEnums;
import com.aurora.common.enums.article.ArticleAuditStateType;
import com.aurora.common.utils.DateUtils;
import com.aurora.common.utils.email.MailUtils;
import com.aurora.system.mapper.TripartiteUserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    /**
     * 更新网站每日数据
     * 每天23点执行
     */
    @Scheduled(cron = "0 0 23 * * ?")
    public void StatInfoUpdate() {
        try {
            log.info("网站数据更新开始：{}", DateUtils.getTime());
            Long articleCount = articleInformationMapper.selectCount(new QueryWrapper<ArticleInformation>().eq("audit_state", ArticleAuditStateType.APPROV.getCode()));
            Long specialCount = specialInformationMapper.selectCount(null);
            Long userCount = tripartiteUserMapper.selectCount(null);
            Long dictumGroupCount = dictumGroupMapper.selectCount(null);
            Long dictumAlbumCount = dictumAlbumMapper.selectCount(null);
            Long dictumInfoCount = dictumInfoMapper.selectCount(null);
            StatDataInfo statDataInfo = new StatDataInfo()
                .setArticleCount(articleCount)
                .setUserCount(userCount)
                .setSpecialCount(specialCount)
                .setStatTime(DateUtils.getDate())
                .setDictumGroupCount(dictumGroupCount)
                .setDictumAlbumCount(dictumAlbumCount)
                .setDictumCount(dictumInfoCount);
            statDataInfoMapper.insertUpdate(statDataInfo);
            log.info("网站数据更新成功：时间：{}，数据：{}", DateUtils.getTime(), statDataInfo);
        } catch (Exception e) {
            e.printStackTrace();
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "网站数据更新（StatInfoUpdate）任务异常", e.getMessage());
            log.error("网站数据更新异常：时间：{}", DateUtils.getTime());
        }
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.UPDATE_WEBSITE_DAILY_DATA.getCode());
    }

    /**
     * 更新网站每月数据
     * 每天0点执行
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void StatTheInfoUpdate() {
        try {
            log.error("网站每月数据更新开始：{}", DateUtils.getTime());
            StatDataInfo statDataInfo = statDataInfoMapper.selectLists(DateUtils.getThe());
            if (statDataInfo == null) return;
            statTheDataMapper.insertLists(statDataInfo);
            log.info("网站每月数据更新结束：时间：{}，数据：{}", DateUtils.getTime(), statDataInfo);
        } catch (Exception e) {
            e.printStackTrace();
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "网站数据更新（StatTheInfoUpdate）任务异常", e.getMessage());
            log.error("网站每月数据更新异常：时间：{}", DateUtils.getTime());
        }
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.UPDATE_WEBSITE_MONTHLY_DATA.getCode());
    }
}
