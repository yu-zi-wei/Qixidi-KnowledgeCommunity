package com.qixidi.business.service;

import com.qixidi.business.domain.entity.ToSiteInfo;
import com.qixidi.business.domain.vo.FriendLinkVo;
import com.qixidi.business.domain.vo.stat.StatDataInfoVo;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
public interface SiteInfoService {
    ToSiteInfo info();

    List<FriendLinkVo> friendLink();

    StatDataInfoVo totalData();
}
