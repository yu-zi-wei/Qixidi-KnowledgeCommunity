package com.qixidi.business.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.special.SpecialInformation;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.special.SpecialInformationMapper;
import com.light.core.constant.SystemConstant;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.light.core.utils.email.MailUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zi-wei
 * @create 2024/11/17 10:31
 */
@Slf4j
@Service
public class SpecialTask {
    @Autowired
    private SpecialInformationMapper specialInformationMapper;
    @Autowired
    private ArticleInformationMapper articleInformationMapper;
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;

    /**
     * 同步专栏文章数
     * 每10分钟执行一次
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void recalculationColumn() {
        log.error("同步专栏文章数开始：{}", DateUtil.date());
        try {
            List<SpecialInformation> specialInformations = specialInformationMapper.selectList(
                new LambdaQueryWrapper<SpecialInformation>().select(SpecialInformation::getId));

            if (CollectionUtil.isEmpty(specialInformations)) return;
            List<Long> specialIds = specialInformations.stream().map(SpecialInformation::getId).collect(Collectors.toList());
            List<ArticleInformation> articleInformations = articleInformationMapper.selectList(new LambdaQueryWrapper<ArticleInformation>()
                .select(ArticleInformation::getId, ArticleInformation::getSpecialId)
                .in(ArticleInformation::getSpecialId, specialIds)
                .eq(ArticleInformation::getState, 0)
                .eq(ArticleInformation::getAuditState, 2)
                .isNotNull(ArticleInformation::getSpecialId));
            if (CollectionUtil.isEmpty(articleInformations)) return;
            Map<Long, Long> collectMap = articleInformations.stream().collect(Collectors.groupingBy(ArticleInformation::getSpecialId, Collectors.counting()));
            List<SpecialInformation> updateSpecialInformations = new ArrayList<>();
            specialInformations.forEach(item -> {
                Long sum = collectMap.get(item.getId());
                if (sum != null) {
                    SpecialInformation specialInformation = new SpecialInformation();
                    specialInformation.setId(item.getId());
                    specialInformation.setIncludedCount(Integer.valueOf(sum.toString()));
                    updateSpecialInformations.add(specialInformation);
                }
            });
            specialInformationMapper.updateBatchById(updateSpecialInformations);
        } catch (Exception e) {
            log.error("同步专栏文章数失败", e);
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "同步专栏文章数(SpecialTask) 任务异常", e.getMessage());
        }
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.SYNC_NUMBER_COLUMNS.getCode());
    }
}
