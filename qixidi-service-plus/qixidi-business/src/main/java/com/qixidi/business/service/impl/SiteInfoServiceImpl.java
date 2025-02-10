package com.qixidi.business.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.entity.FriendLink;
import com.qixidi.business.domain.entity.ToSiteInfo;
import com.qixidi.business.domain.vo.FriendLinkVo;
import com.qixidi.business.mapper.FriendLinkMapper;
import com.qixidi.business.mapper.ToSiteInfoMapper;
import com.qixidi.business.service.SiteInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
@Service
public class SiteInfoServiceImpl extends ServiceImpl<ToSiteInfoMapper, ToSiteInfo> implements SiteInfoService {
    @Autowired
    private FriendLinkMapper friendLinkMapper;

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
}
