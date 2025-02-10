package com.qixidi.business.task;


import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.domain.vo.user.UserFollowVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.light.core.constant.SystemConstant;
import com.light.core.enums.SystemTaskEnums;
import com.light.core.utils.DateUtils;
import com.light.core.utils.email.MailUtils;
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
    private UserFollowMapper userFollowMapper;//关注
    @Autowired
    private ArticleInformationMapper articleInformationMapper;//文章
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;

    /**
     * 同步标签信息数据
     * 每20分钟一次
     */
    @Scheduled(cron = "0 */20 * * * *")
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
            labelInfoMapper.updateTaskList(list1);
            log.info("同步标签信息数据成功：时间：{}", DateUtils.getTime());
        } catch (Exception e) {
            MailUtils.sendText(SystemConstant.AdministratorMailboxList, "同步标签信息数据（syncLabel）任务异常", e.getMessage());
            log.error("同步标签信息数据异常：时间：{}", DateUtils.getTime());
            e.printStackTrace();
        }
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.SYNCHRONIZE_b_label_infoRMATION_DATA.getCode());
    }
}
