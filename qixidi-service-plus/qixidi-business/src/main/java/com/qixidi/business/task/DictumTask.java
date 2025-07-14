package com.qixidi.business.task;


import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.light.core.constant.SystemConstant;
import com.light.core.utils.DateUtils;
import com.light.core.utils.email.MailUtils;
import com.light.redission.utils.RedisUtils;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.SystemTaskEnums;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.mapper.dictum.DictumAlbumMapper;
import com.qixidi.business.mapper.dictum.DictumGroupMapper;
import com.qixidi.business.mapper.dictum.DictumInfoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DictumTask {

    private final DictumAlbumMapper dictumAlbumMapper;
    private final DictumGroupMapper dictumGroupMapper;
    private final DictumInfoMapper dictumInfoMapper;
    @Autowired
    private SystemTaskConfigMapper systemTaskConfigMapper;

    /**
     * 同步名言数据
     * 每半小时一次
     */
    @Scheduled(cron = "0 */30 * * * *")
    public void dictumDataShn() {
        try {
            List<DictumInfoVo> dictumInfoVos = dictumInfoMapper.selectGroupId();
            dictumGroupMapper.updateList(dictumInfoVos);
        } catch (Exception e) {
            e.printStackTrace();
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "名言分类数据同步异常（dictumDataShn）任务异常", e.getMessage());
        }
        try {
            List<DictumInfoVo> albumId = dictumInfoMapper.selectAlbumId();
            dictumAlbumMapper.updateList(albumId);

        } catch (Exception e) {
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "专辑分类数据同步异常（dictumDataShn）任务异常", e.getMessage());
            e.printStackTrace();
        }
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.SYNCING_QUOTES_DATA.getCode());

    }

    /**
     * 计算名言广场热门数据（作者、标签）
     * 每10分钟一次
     */
    @Scheduled(cron = "0 */10 * * * *")
    public void countPopularAuthors() {
        try {
            List<DictumInfoVo> listAuthors = dictumInfoMapper.selectAuthorAuthors();
            if (CollectionUtils.isNotEmpty(listAuthors)) {
                // 获取作者出现的频率，并按照出现次数降序排列
                List<Map<String, String>> collect = listAuthors.stream()
                        .map(item -> item.getAuthor()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet().stream()
                        .sorted(Comparator.comparing(e -> e.getValue(), Comparator.reverseOrder()))
                        .map(e -> {
                            Map<String, String> map = new HashMap<>();
                            map.put("author", String.valueOf(e.getKey()));
                            map.put("count", String.valueOf(e.getValue()));
                            return map;
                        }).collect(Collectors.toList());
//        默认存前20个
                Set<Map<String, String>> set = collect.stream().limit(20).collect(Collectors.toSet());
                RedisUtils.deleteObject(RedisBusinessKeyEnums.POPULAR_AUTHORS.getKey());
                RedisUtils.setCacheSet(RedisBusinessKeyEnums.POPULAR_AUTHORS.getKey(), set);
                log.info("热门作者数据同步成功：时间：{}，数据：{}", DateUtils.getTime(), set);
            }
            List<DictumInfoVo> listLabel = dictumInfoMapper.selectAuthorLabel();
            if (CollectionUtils.isNotEmpty(listLabel)) {
//        计算热门标签
                List<String> listLabels = new ArrayList<>();
                listLabel.forEach(item -> {
                    if (ObjectUtils.isNotEmpty(item.getLabel())) {
                        List<String> list1 = Arrays.asList(item.getLabel().split(","));
                        listLabels.addAll(list1);
                    }
                });
                List<Map<String, String>> collectLabel = listLabels.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet().stream()
                        .sorted(Comparator.comparing(e -> e.getValue(), Comparator.reverseOrder()))
                        .map(e -> {
                            Map<String, String> map = new HashMap<>();
                            map.put("label", String.valueOf(e.getKey()));
                            map.put("count", String.valueOf(e.getValue()));
                            return map;
                        }).collect(Collectors.toList());

                //        默认存前20个
                Set<Map<String, String>> setLabel = collectLabel.stream().limit(20).collect(Collectors.toSet());
                RedisUtils.deleteObject(RedisBusinessKeyEnums.POPULAR_LABEL.getKey());
                RedisUtils.setCacheSet(RedisBusinessKeyEnums.POPULAR_LABEL.getKey(), setLabel);
                log.info("热门标签数据同步成功：时间：{}，数据：{}", DateUtils.getTime(), setLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MailUtils.sendText(SystemConstant.getAdministratorMailboxList(), "热门作者（标签）同步异常（countPopularAuthors）任务异常", e.getMessage());
            log.error("热门作者（标签）数据同步异常：时间：{}", DateUtils.getTime());
        }
        systemTaskConfigMapper.addExecutionSum(SystemTaskEnums.CALCULATING_HOT_DATA.getCode());
    }


}
