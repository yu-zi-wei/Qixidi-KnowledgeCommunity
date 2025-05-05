package com.qixidi.business.service.impl.stat;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.light.core.utils.DateUtils;
import com.qixidi.business.domain.entity.stat.StatDataInfo;
import com.qixidi.business.domain.enums.StatDataEnums;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.domain.vo.stat.StatDataInfoVo;
import com.qixidi.business.domain.vo.stat.StatReturnDataVo;
import com.qixidi.business.domain.vo.stat.StatTheDataVo;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.stat.StatDataInfoMapper;
import com.qixidi.business.mapper.stat.StatTheDataMapper;
import com.qixidi.business.service.stat.IStatDataInfoService;
import com.qixidi.business.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public StatDataInfoVo queryPageList(StatDataInfoVo bo) {
        if (ObjectUtil.isNull(bo.getStatTime())) {
            bo.setStatTime(DateUtils.getDate());
        }
        Long labelCount = labelInfoMapper.selectCount(null);
        Long labelGroupCount = labelGroupingInfoMapper.selectCount(null);
        StatDataInfoVo statData = baseMapper.selectVoOne(new LambdaQueryWrapper<StatDataInfo>().eq(StatDataInfo::getStatTime, bo.getStatTime()));
        if (ObjectUtil.isNull(statData)) {
            statData = baseMapper.selectVoOne(new QueryWrapper<StatDataInfo>().orderByDesc("id").last("limit 1"));
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
                if (classMap.containsKey(statDataEnums.getCode())) {
                    StatReturnDataVo statReturnDataVo = new StatReturnDataVo(statDataEnums.getValue(), item.getStatTime(),
                            Long.valueOf(classMap.get(statDataEnums.getCode()).toString()));
                    statReturnDataVos.add(statReturnDataVo);
                }
            }
        });
        return statReturnDataVos;
    }

    @Override
    public Map<String, Object> labelDate(StatDataInfoVo bo) {
        Map<String, Object> map = new HashMap<>();
        List<StatReturnDataVo> statReturnDataVos = labelGroupingInfoMapper.selectStatData(bo);
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
        map.put("labelGroup", statReturnDataVos);
        map.put("label", lists);
        return map;
    }

}
