package com.qixidi.business.mapper.user;

import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.label.LabelInfoVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.domain.vo.user.UserFollowVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
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

