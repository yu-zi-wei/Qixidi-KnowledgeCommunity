package com.qixidi.business.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.light.core.constant.SystemConstant;
import com.qixidi.business.domain.enums.CommentType;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.light.core.utils.AlgorithmUtils;
import com.light.core.utils.DateUtils;
import com.light.core.utils.email.MailUtils;
import com.light.redission.utils.RedisUtils;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.fabulous.FabulousRecord;
import com.qixidi.business.domain.vo.article.ArticleInformationVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.fabulous.FabulousRecordMapper;
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
    private FabulousRecordMapper fabulousRecordMapper;
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
                MailUtils.sendText(SystemConstant.AdministratorMailboxList, "线程超时", "计算文章权重任务超时！ArticleCalculateWeight");
                log.error("计算文章权重任务超时,{}", pool.getClass().getName());
                return false;
            }
            return true;
        } catch (InterruptedException e) {
            // awaitTermination方法被中断的时候也中止线程池中全部的线程的执行。
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "线程异常中断", "计算文章权重任务异常中断！ArticleCalculateWeight");
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
            BeanUtils.copyProperties(item, articleInformation);
            Map<String, Integer> datePoor = DateUtils.getDatePoor(item.getCreateTime(), new Date());
            Integer day = datePoor.get("day");
            double heatWeight = (articleInformation.getLikeTimes() * 2)
                    + (articleInformation.getCommentTimes() * 3)
                    + (articleInformation.getCollectionTimes() * 3)
                    + (articleInformation.getNumberTimes() * 1)
                    + (AlgorithmUtils.directionExport(day));
            log.info("点赞次数：{}，评论次数：{}，收藏次数：{}，浏览：{}，时间蹉：{}", articleInformation.getLikeTimes()
                    , articleInformation.getCommentTimes(), articleInformation.getCollectionTimes(), articleInformation.getNumberTimes()
                    , AlgorithmUtils.directionExport(day));
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
    public void ArticleCalculateWeight() {
        Long aLong = articleInformationMapper.selectCount(new QueryWrapper<ArticleInformation>()
                .eq("state", 0).eq("audit_state", 2));
        log.info("计算文章权重任务开始,文章总条数：{}", aLong);
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
                MailUtils.sendText(SystemConstant.AdministratorMailboxList, "计算文章权重任务异常",
                        String.format("{}，发生时间：{}", "计算文章权重任务异常！ArticleCalculateWeight", DateUtil.formatDateTime(new Date())));
                log.error("计算文章权重任务异常,异常信息：{}", e.getMessage());
                state = false;
            }
        }
        log.info("计算文章权重任务结束,最后一次文章ID：{}", pageId);
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.CALCULATING_ARTICLE_WEIGHTS.getCode());
    }

    /**
     * 同步文章点赞
     * 每周天晚上0点执行
     */
    @Scheduled(cron = "0 0 0 ? * SUN")
    public void syncFabulous() {
        log.info("文章点赞开始同步");
//         用户点赞的文章列表
        Map<String, Set<String>> cacheMpa = RedisUtils.getCacheMap(RedisBusinessKeyEnums.USER_LIKE_ARTICLE_KEY.getKey());
//         文章点赞人列表
        Map<String, Set<String>> articleMap = RedisUtils.getCacheMap(RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey());
//      文章点赞总数
        Map<String, Object> cacheMap = RedisUtils.getCacheMap(RedisBusinessKeyEnums.TOTAL_LIKE_COUNT_KEY.getKey());
        if (CollectionUtils.isNotEmpty(cacheMpa)) {
//            文章点赞记录落盘
            syncArticleFl(articleMap, cacheMap);
            RedisUtils.deleteObject(RedisBusinessKeyEnums.USER_LIKE_ARTICLE_KEY.getKey());
            RedisUtils.deleteObject(RedisBusinessKeyEnums.ARTICLE_LIKED_USER_KEY.getKey());
            RedisUtils.deleteObject(RedisBusinessKeyEnums.TOTAL_LIKE_COUNT_KEY.getKey());
        }
        log.info("文章点赞同步结束");
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.SYNC_ARTICLE_CLICK_LIKE.getCode());
    }

    /**
     * @param articleMap 文章点赞人列表
     * @param cacheMap   文章点赞总数
     */
    private void syncArticleFl(Map<String, Set<String>> articleMap, Map<String, Object> cacheMap) {
        executorService.execute(() -> {
            try {
                //同步点赞记录
                articleMap.forEach((k, v) -> {
                    List<FabulousRecord> list = new ArrayList<>();
                    for (String uid : v) {
                        FabulousRecord fabulousRecord = new FabulousRecord();
                        fabulousRecord.setTypeId(Long.valueOf(k));
                        fabulousRecord.setUid(uid);
                        fabulousRecord.setCreateTime(new Date());
                        fabulousRecord.setType(CommentType.ARTICLE_TYPE.getCode());
                        list.add(fabulousRecord);
                    }
//                        同步点赞表
                    fabulousRecordMapper.insertBatch(list);
                    log.info("点赞表同步成功" + DateUtils.getTime());
                });
                //                        同步文章点赞数
                List<ArticleInformation> articleInformation = new ArrayList<>();
                cacheMap.forEach((k, v) -> {
                    ArticleInformation articleInformation1 = new ArticleInformation();
                    articleInformation1.setId(Long.valueOf(k)).setLikeTimes(Long.valueOf(v.toString()));
                    articleInformation.add(articleInformation1);
                });
                articleInformationMapper.updateBatchById(articleInformation);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("点赞同步 执行异常异常：{}", e.getMessage());
//                    发送异常邮件
                MailUtils.sendText(SystemConstant.AdministratorMailboxList, "点赞信息同步异常->定时任务：syncFabulous", e.getMessage());
            }
        });
    }
}
