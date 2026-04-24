package com.qixidi.business.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.light.core.constant.SystemConstant;
import com.light.core.utils.AlgorithmUtils;
import com.light.core.utils.DateUtils;
import com.light.core.utils.email.MailUtils;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ArticleTask {

    @Autowired
    private ArticleInformationMapper articleInformationMapper;//文章
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;

    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;

    /**
     * 有返回值处理
     *
     * @param pool    线程池
     * @param timeout 超时时间
     * @param unit    超时单位
     * @return
     */
    public static Boolean shutdown(ExecutorService pool, long timeout, TimeUnit unit) {
        try {
            //等待所有的任务都结束（实时判断是否全完成），若所有任务都已完成，则返回true，若超时未完成，则返回false
            if (!pool.awaitTermination(timeout, unit)) {
                MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "线程超时", "计算文章权重任务超时！ArticleCalculateWeight");
                log.error("计算文章权重任务超时,{}", pool.getClass().getName());
                return false;
            }
            return true;
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "线程异常中断", "计算文章权重任务异常中断！ArticleCalculateWeight");
            log.error("计算文章权重任务异常中断,{}", pool.getClass().getName());
            return false;
        }
    }

    /**
     * 递归查询数据库
     *
     * @return
     */
    public Map<String, Object> recurrence(Long pageId, Long size) throws InterruptedException {
        Map<String, Object> map = new HashMap();
        List<ArticleInformationVo> list = articleInformationMapper.selectData(pageId, size);
//        获取最大id数
        Long pageIds = list.stream().map(ArticleInformationVo::getId).max(Long::compareTo).get();
        executorService.execute(() -> {
//                计算权重
            this.articleWeightAlgorithm(list);
        });
//            接受线程返回结果
        Thread.sleep(1000);
//        Boolean shutdown = shutdown(executorService, 40, TimeUnit.SECONDS);
        map.put("states", true);
        map.put("pageIds", pageIds);
        return map;
    }

    public void articleWeightAlgorithm(List<ArticleInformationVo> list) {
        List<ArticleInformation> heatWeightList = list.stream().map(item -> {
            ArticleInformation articleInformation = new ArticleInformation();
            articleInformation.setId(item.getId());
            Map<String, Integer> datePoor = DateUtils.getDatePoor(item.getCreateTime(), new Date());
            Integer day = datePoor.get("day");
            double heatWeight = (item.getLikeTimes() == null ? 0 : item.getLikeTimes())
                    + (item.getCommentTimes() == null ? 0 : item.getCommentTimes() * 2)
                    + (item.getCollectionTimes() == null ? 0 : item.getCollectionTimes() * 2)
                    + (item.getNumberTimes() == null ? 0 : item.getNumberTimes())
                    + (AlgorithmUtils.directionExport(day));
            articleInformation.setHeatWeight(heatWeight);
            return articleInformation;
        }).collect(Collectors.toList());
//        同步数据库
        Integer i = articleInformationMapper.updateListHeatWeight(heatWeightList);
    }

    /**
     * 计算文章权重
     * 每小时执行一次
     * 1、每200条数据一次计算
     * 2、占比：
     * 3、批量修改数据
     * 4、
     */
    @Scheduled(cron = "0 0 * * * ?")
//    @Scheduled(cron = "0 */2 * * * ?")
    public void ArticleCalculateWeight() {
        Long aLong = articleInformationMapper.selectCount(new LambdaQueryWrapper<ArticleInformation>()
                .eq(ArticleInformation::getState, 0).eq(ArticleInformation::getAuditState, 2));
        Boolean state = true;
        //当前最大文章id
        Long pageId = 0L;
//        记录查询总数
        Long sizeSum = 0L;
//        每次查询条数
        Long size = 200L;
        while (state) {
            state = false;
            try {
                if (sizeSum < aLong) {
                    sizeSum += size;
                    Map<String, Object> recurrence = this.recurrence(pageId, size);
                    Boolean states = (Boolean) recurrence.get("states");
                    Long pageIds = (Long) recurrence.get("pageIds");
                    state = states;
                    pageId = pageIds;
                } else {
                    state = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("计算文章权重 执行异常异常：{}", e.getMessage());
                MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "计算文章权重任务异常",
                        String.format("{}，发生时间：{}", "计算文章权重任务异常！ArticleCalculateWeight", DateUtil.formatDateTime(new Date())));
                log.error("计算文章权重任务异常,异常信息：{}", e.getMessage());
                state = false;
            }
        }
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.CALCULATING_ARTICLE_WEIGHTS.getCode());
    }
}
