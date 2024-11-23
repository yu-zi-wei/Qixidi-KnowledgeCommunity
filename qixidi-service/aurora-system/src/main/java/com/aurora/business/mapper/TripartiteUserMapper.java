package com.aurora.business.mapper;

import com.aurora.business.domain.bo.user.UserInfoBo;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.mapper.BaseMapperPlus;
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

    @Select("select a_currency from user_main where uuid=#{uuid}")
    Long selectAcurrency(@Param("uuid") String uuid);

    @Select("select nickname,email from user_main where phone=#{phone} and state=0")
    TripartiteUser selectPhone(@Param("phone") String phone);

    List<TripartiteUserVo> selectPrivacy();
}
