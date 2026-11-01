package com.qixidi.business.service.impl.stat;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.light.core.utils.DateUtils;
import com.qixidi.business.domain.entity.article.ArticleInformation;
import com.qixidi.business.domain.entity.label.LabelGroupingInfo;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.entity.stat.StatDataInfo;
import com.qixidi.business.domain.enums.StatDataEnums;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.domain.vo.stat.StatDataInfoVo;
import com.qixidi.business.domain.vo.stat.StatReturnDataVo;
import com.qixidi.business.domain.vo.stat.StatTheDataVo;
import com.qixidi.business.mapper.article.ArticleInformationMapper;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.stat.StatDataInfoMapper;
import com.qixidi.business.mapper.stat.StatTheDataMapper;
import com.qixidi.business.service.stat.IStatDataInfoService;
import com.qixidi.business.utils.ClassUtils;
import com.qixidi.common.domain.enums.StatusEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计数据信息Service业务层处理
 *
 * @author aurora
 * @date 2023-03-14
 */
@RequiredArgsConstructor
@Service
public class StatDataInfoServiceImpl implements IStatDataInfoService {

    private final StatDataInfoMapper baseMapper;
    private final StatTheDataMapper statTheDataMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final LabelGroupingInfoMapper labelGroupingInfoMapper;
    private final ArticleInformationMapper articleInformationMapper;

    @Override
    public StatDataInfoVo queryPageList(StatDataInfoVo bo) {
        if (ObjectUtil.isNull(bo.getStatTime())) {
            bo.setStatTime(DateUtils.getDate());
        }
        Long labelCount = labelInfoMapper.selectCount(new LambdaQueryWrapper<LabelInfo>().eq(LabelInfo::getState, StatusEnums.NORMAL.getCode()));
        Long labelGroupCount = labelGroupingInfoMapper.selectCount(new LambdaQueryWrapper<LabelGroupingInfo>().eq(LabelGroupingInfo::getState, StatusEnums.NORMAL.getCode()));
        StatDataInfoVo statData = baseMapper.selectVoOne(new LambdaQueryWrapper<StatDataInfo>().eq(StatDataInfo::getStatTime, bo.getStatTime()));
        if (ObjectUtil.isNull(statData)) {
            statData = baseMapper.selectVoOne(new LambdaQueryWrapper<StatDataInfo>()
                    .orderByDesc(StatDataInfo::getId).last("limit 1"));
        }
        statData.setLabelCount(labelCount);
        statData.setLabelGroupCount(labelGroupCount);
        return statData;
    }

    @Override
    public List<StatReturnDataVo> theList(StatDataInfoVo bo) {
        List<StatTheDataVo> statTheDataVos = statTheDataMapper.selectVoLists();
        List<StatReturnDataVo> statReturnDataVos = new ArrayList<>();
        StatDataEnums[] StatDataEnumsList = StatDataEnums.values();
        statTheDataVos.forEach(item -> {
            for (StatDataEnums statDataEnums : StatDataEnumsList) {
                Map<String, Object> classMap = ClassUtils.reflect(item);
                //reflect 把 null 字段也放进 map（containsKey 恒真）；历史行计数列可能为 NULL
                //（早期 INSERT 裸值落库），null 直接跳过该点，防止 toString() NPE
                Object value = classMap.get(statDataEnums.getCode());
                if (value != null) {
                    StatReturnDataVo statReturnDataVo = new StatReturnDataVo(statDataEnums.getValue(), item.getStatTime(),
                            Long.valueOf(value.toString()));
                    statReturnDataVos.add(statReturnDataVo);
                }
            }
        });
        return statReturnDataVos;
    }

    @Override
    public Map<String, Object> labelDate(StatDataInfoVo bo) {
        Map<String, Object> map = new HashMap<>();
        map.put("labelGroup", groupingArticleCounts());
        List<LabelInfoVo> list = labelInfoMapper.selectVoList(null);
        List<StatReturnDataVo> lists = new ArrayList<>();
        list.forEach(item -> {
            for (int i = 0; i < 2; i++) {
                StatReturnDataVo statReturnDataVo = new StatReturnDataVo()
                        .setName(item.getLabelName())
                        .setTwoName(i == 0 ? "文章数" : "关注数")
                        .setData(Long.valueOf(i == 0 ? item.getArticleNumber().toString() : item.getFollowNumber().toString()));
                lists.add(statReturnDataVo);
            }
        });
        map.put("label", lists);
        return map;
    }

    /**
     * 各分组文章数（原单条 SQL join 文章表改为两步单表：查有效分组后批量查文章域按 grouping_id 内存计数；
     * 保持原 GROUP BY grouping_name 的同名分组合并语义）
     */
    private List<StatReturnDataVo> groupingArticleCounts() {
        List<LabelGroupingInfo> groupings = labelGroupingInfoMapper.selectList(
                new LambdaQueryWrapper<LabelGroupingInfo>().eq(LabelGroupingInfo::getState, 0));
        if (groupings.isEmpty()) {
            return new ArrayList<>();
        }
        Map<Long, Long> articleCounts = articleInformationMapper.selectList(
                        new LambdaQueryWrapper<ArticleInformation>()
                                .select(ArticleInformation::getId, ArticleInformation::getGroupingId)
                                .in(ArticleInformation::getGroupingId,
                                        groupings.stream().map(LabelGroupingInfo::getId).toList()))
                .stream()
                .filter(a -> a.getGroupingId() != null)
                .collect(Collectors.groupingBy(ArticleInformation::getGroupingId, Collectors.counting()));
        Map<String, Long> byName = new LinkedHashMap<>();
        groupings.forEach(g -> byName.merge(g.getGroupingName(), articleCounts.getOrDefault(g.getId(), 0L), Long::sum));
        return byName.entrySet().stream()
                .map(e -> new StatReturnDataVo(e.getKey(), null, e.getValue()))
                .toList();
    }

}
