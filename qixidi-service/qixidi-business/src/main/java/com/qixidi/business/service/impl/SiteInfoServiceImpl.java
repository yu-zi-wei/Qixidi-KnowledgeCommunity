package com.qixidi.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qixidi.business.domain.entity.FriendLink;
import com.qixidi.business.domain.entity.ToSiteInfo;
import com.qixidi.business.domain.entity.label.LabelGroupingInfo;
import com.qixidi.business.domain.entity.label.LabelInfo;
import com.qixidi.business.domain.entity.stat.StatDataInfo;
import com.qixidi.business.domain.vo.FriendLinkVo;
import com.qixidi.business.domain.vo.stat.StatDataInfoVo;
import com.qixidi.business.mapper.FriendLinkMapper;
import com.qixidi.business.mapper.ToSiteInfoMapper;
import com.qixidi.business.mapper.label.LabelGroupingInfoMapper;
import com.qixidi.business.mapper.label.LabelInfoMapper;
import com.qixidi.business.mapper.stat.StatDataInfoMapper;
import com.qixidi.business.service.SiteInfoService;
import com.qixidi.common.domain.enums.StatusEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
@RequiredArgsConstructor
@Service
public class SiteInfoServiceImpl extends ServiceImpl<ToSiteInfoMapper, ToSiteInfo> implements SiteInfoService {
    private final FriendLinkMapper friendLinkMapper;
    private final StatDataInfoMapper statDataInfoMapper;
    private final LabelInfoMapper labelInfoMapper;
    private final LabelGroupingInfoMapper labelGroupingInfoMapper;

    @Override
    public ToSiteInfo info() {
        return baseMapper.selectById(1);
    }

    @Override
    public List<FriendLinkVo> friendLink() {
        List<FriendLink> friendLinks = friendLinkMapper.selectList(new LambdaQueryWrapper<FriendLink>().eq(FriendLink::getStatus, 0));
        List<FriendLinkVo> friendLinkVos = BeanUtil.copyToList(friendLinks, FriendLinkVo.class);
        return friendLinkVos;
    }

    @Override
    public StatDataInfoVo totalData() {
        StatDataInfoVo statDataInfoVo = statDataInfoMapper.selectVoOne(new LambdaQueryWrapper<StatDataInfo>()
                .eq(StatDataInfo::getStatTime, DateUtil.today()));
        Long labelCount = labelInfoMapper.selectCount(new LambdaQueryWrapper<LabelInfo>().eq(LabelInfo::getState, StatusEnums.NORMAL.getCode()));
        Long labelGroupCount = labelGroupingInfoMapper.selectCount(new LambdaQueryWrapper<LabelGroupingInfo>().eq(LabelGroupingInfo::getState, StatusEnums.NORMAL.getCode()));

        if (statDataInfoVo == null) {
            statDataInfoVo = statDataInfoMapper.selectVoOne(new LambdaQueryWrapper<StatDataInfo>()
                    .orderByDesc(StatDataInfo::getId).last("limit 1"));
        }
        statDataInfoVo.setLabelCount(labelCount);
        statDataInfoVo.setLabelGroupCount(labelGroupCount);
        return statDataInfoVo;
    }
}
