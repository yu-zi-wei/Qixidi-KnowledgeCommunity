package com.qixidi.business.comtroller.backstage;

import com.qixidi.business.domain.vo.SystemTaskConfigVo;
import com.qixidi.business.service.SystemTaskConfigService;
import com.qixidi.business.task.*;
import com.light.core.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统任务
 *
 * @author zi-wei
 * @create 2024/11/17 13:45
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/task")
public class SystemTaskController {

    private final SystemTaskConfigService systemTaskConfigService;
    private final ArticleTask articleTask;
    private final DictumTask dictumTask;
    private final LabelTask labelTask;
    private final SpecialTask specialTask;
    private final StatTask statTask;
    private final UserTask userTask;

    /**
     * 获取任务列表
     *
     * @return
     */
    @GetMapping("/list")
    public R<List<SystemTaskConfigVo>> systemTaskList() {
        return R.ok(systemTaskConfigService.list());
    }

    /**
     * 计算文章权重任务
     *
     * @return
     */
    @GetMapping("/calculating_article_weights")
    public void calculatingArticleWeights() {
        articleTask.ArticleCalculateWeight();
    }


    /**
     * 同步文章点赞任务
     *
     * @return
     */
    @GetMapping("/sync_article_click_like")
    public void syncArticleClickLike() {
        articleTask.syncFabulous();
    }

    /**
     * 同步名言数据任务
     *
     * @return
     */
    @GetMapping("/syncing_quotes_data")
    public void syncingQuotesData() {
        dictumTask.dictumDataShn();
    }

    /**
     * 计算热门数据任务
     *
     * @return
     */
    @GetMapping("/calculating_hot_data")
    public void calculatingHotData() {
        dictumTask.countPopularAuthors();
    }

    /**
     * 同步标签信息数据任务
     *
     * @return
     */
    @GetMapping("/synchronize_label_information_data")
    public void synchronizeLabelInformationData() {
        labelTask.syncLabel();
    }

    /**
     * 同步专栏文章数任务
     *
     * @return
     */
    @GetMapping("/sync_number_columns")
    public void syncNumberColumns() {
        specialTask.recalculationColumn();
    }

    /**
     * 更新网站每日数据任务
     *
     * @return
     */
    @GetMapping("/update_website_daily_data")
    public void updateWebsiteDailyData() {
        statTask.StatInfoUpdate();
    }

    /**
     * 更新网站每月数据任务
     *
     * @return
     */
    @GetMapping("/update_website_monthly_data")
    public void updateWebsiteMonthlyData() {
        statTask.StatTheInfoUpdate();
    }

    /**
     * 用户网站数据同步任务
     *
     * @return
     */
    @GetMapping("/syncing_user_site_data")
    public void syncingUserSiteData() {
        userTask.useWebsiteSync();
    }

}
