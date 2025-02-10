package com.qixidi.business.service.user;

import com.qixidi.business.domain.bo.user.UserFollowBo;

/**
 * 用户关注Service接口
 *
 * @author aurora
 * @date 2023-02-13
 */
public interface IUserFollowService {

    boolean insertByBo(UserFollowBo bo);

    boolean cancelFollow(UserFollowBo bo);

    Object followList(String uid, Integer type);

    Object followRoleList(Integer type);
}
