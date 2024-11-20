package com.aurora.business.mapper.user;

import com.aurora.business.domain.entity.user.UserFollow;
import com.aurora.business.domain.vo.CountUserWebsiteVo;
import com.aurora.business.domain.vo.label.LabelInfoVo;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.business.domain.vo.user.UserFollowVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户关注Mapper接口
 *
 * @author aurora
 * @date 2023-02-13
 */
@Mapper
public interface UserFollowMapper extends BaseMapperPlus<UserFollowMapper, UserFollow, UserFollowVo> {

    List<LabelInfoVo> followLabelList(@Param("uid") String uid, @Param("type") Integer type);

    List<TripartiteUserVo> followUserList(@Param("uid") String uid, @Param("type") Integer type);

    List<CountUserWebsiteVo> selectFollowTask();

    List<UserFollowVo> selectVoLabelGroup();

}

