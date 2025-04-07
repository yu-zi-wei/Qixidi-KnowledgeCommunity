package com.qixidi.business.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.business.domain.bo.user.UserInfoBo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 平台用户Mapper接口
 *
 * @author ziwei
 * @date 2022-06-12
 */
@Mapper
public interface TripartiteUserMapper extends BaseMapperPlus<TripartiteUserMapper, TripartiteUser, TripartiteUserVo> {

    TripartiteUserVo selectWebInfo(@Param("uuid") String tripartiteUuid);

    TripartiteUserVo getBasicsUser(@Param("uuid") String uuid);

    List<TripartiteUserVo> fdUserList(@Param("bo") UserInfoBo bo);

    int updateACurrency(@Param("uid") String uid, @Param("rechargeCurrency") Long rechargeCurrency);

    @Select("select a_currency from b_user_main where uuid=#{uuid}")
    Long selectAcurrency(@Param("uuid") String uuid);

    @Select("select nickname,email from b_user_main where phone=#{phone} and state=0")
    TripartiteUser selectPhone(@Param("phone") String phone);

    @Select("select nickname,email from b_user_main where email=#{email} and state=0")
    TripartiteUser selectEmail(@Param("email") String email);

    List<TripartiteUserVo> selectPrivacy();
}
