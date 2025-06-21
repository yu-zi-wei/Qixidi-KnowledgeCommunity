package com.qixidi.business.task;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.light.core.constant.SystemConstant;
import com.light.core.utils.DateUtils;
import com.light.core.utils.email.MailUtils;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.label.LabelGroupingInfo;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.qixidi.business.domain.enums.article.ArticleAuditStateEnums;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.domain.vo.user.UserFollowVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.qixidi.common.domain.enums.StatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LabelTask {

    @Autowired
    private LabelInfoMapper labelInfoMapper;//标签
    @Autowired
    private LabelGroupingInfoMapper labelGroupingInfoMapper;
    @Autowired
    private UserFollowMapper userFollowMapper;//关注
    @Autowired
    private ArticleInformationMapper articleInformationMapper;//文章
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;

    /**
     * 同步标签信息数据
     * 每20分钟一次
     */
//    @Scheduled(cron = "0 */20 * * * *")
    @Scheduled(cron = "0 */1 * * * *")
    public void syncLabel() {
        try {
            List<UserFollowVo> userFollowVos = userFollowMapper.selectVoLabelGroup();
            Map<String, Integer> collect1 = userFollowVos.stream().collect(Collectors.toMap(UserFollowVo::getTargetId, UserFollowVo::getSum));
            List<String> listLabel = articleInformationMapper.selectLabel();
            List<String> strings = new ArrayList<>();
            listLabel.forEach(item -> {
                strings.addAll(Arrays.asList(item.split(",")));
            });
            // 获取标签出现的次数
            List<Map<String, String>> collect = strings.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                    .entrySet().stream()
                    .sorted(Comparator.comparing(e -> e.getValue(), Comparator.reverseOrder()))
                    .map(e -> {
                        Map<String, String> map = new HashMap<>();
                        map.put("label", String.valueOf(e.getKey()));
                        map.put("count", String.valueOf(e.getValue()));
                        return map;
                    }).collect(Collectors.toList());

            List<LabelInfoVo> list1 = new ArrayList<>();
            collect.forEach(item -> {
                LabelInfoVo infoVo = new LabelInfoVo();
                String labelId = item.get("label");
                if (collect1.get(labelId) != null) {
                    infoVo.setFollowNumber(collect1.get(labelId));
                }
                infoVo.setId(Long.valueOf(labelId));
                infoVo.setArticleNumber(Integer.valueOf(item.get("count")));
                list1.add(infoVo);
            });
            //清理数据
            labelInfoMapper.update(new LambdaUpdateWrapper<LabelInfo>()
                    .set(LabelInfo::getArticleNumber, 0)
                    .set(LabelInfo::getFollowNumber, 0)
                    .eq(LabelInfo::getState, 0));
            labelInfoMapper.updateTaskList(list1);
            log.info("同步标签信息数据成功：时间：{}", DateUtils.getTime());

            systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.SYNCHRONIZE_b_label_infoRMATION_DATA.getCode());
        } catch (Exception e) {
//            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "同步标签信息数据（syncLabel）任务异常", e.getMessage());
            log.error("同步标签信息数据异常：时间：{}", DateUtils.getTime());
            e.printStackTrace();
        }
    }

    /**
     * 同步标签分组文章数据
     */
    @Scheduled(cron = "0 */45 * * * *")
    public void syncLabelGroup() {
        try {
            List<ArticleInformation> articleInformations = articleInformationMapper.selectList(new LambdaQueryWrapper<ArticleInformation>()
                    .select(ArticleInformation::getGroupingId, ArticleInformation::getId)
                    .eq(ArticleInformation::getState, StatusEnums.NORMAL.getCode())
                    .eq(ArticleInformation::getAuditState, ArticleAuditStateEnums.APPROV.getCode()));
            Map<Long, Long> map = articleInformations.stream().collect(Collectors.groupingBy(ArticleInformation::getGroupingId, Collectors.counting()));
            List<LabelGroupingInfo> labelGroupingInfos = labelGroupingInfoMapper.selectList(null);
            labelGroupingInfos.forEach(item -> {
                Long sum = map.get(item.getId());
                if (sum != null) {
                    item.setEntriesNumber(sum);
                }
            });
            labelGroupingInfoMapper.updateById(labelGroupingInfos);
            log.info("同步标签分组文章数据成功：时间：{}", DateUtils.getTime());
        } catch (Exception e) {
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "同步标签分组文章数据（syncLabelGroup）任务异常", e.getMessage());
            log.error("同步标签分组文章数据异常：时间：{}", DateUtils.getTime());
            e.printStackTrace();
        }
    }
}
