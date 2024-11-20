package com.aurora.business.service;

import com.aurora.business.domain.entity.ToSiteInfo;
import com.aurora.business.domain.vo.FriendLinkVo;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
public interface SiteInfoService {
    ToSiteInfo info();

    List<FriendLinkVo> friendLink();

}
