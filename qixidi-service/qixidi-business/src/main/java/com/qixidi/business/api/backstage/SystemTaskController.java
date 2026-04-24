package com.qixidi.business.api.backstage;

import com.qixidi.business.domain.vo.SystemTaskConfigVo;
import com.qixidi.business.service.SystemTaskConfigService;
import com.qixidi.business.task.ArticleTask;
import com.qixidi.business.task.DictumTask;
import com.qixidi.business.task.LabelTask;
import com.qixidi.business.task.StatTask;
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
    private final StatTask statTask;

    /**
     * 获取任务列表
     *
     * @return
     */
    @GetMapping("/list")
    public List<SystemTaskConfigVo> systemTaskList() {
        return systemTaskConfigService.list();
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

}
