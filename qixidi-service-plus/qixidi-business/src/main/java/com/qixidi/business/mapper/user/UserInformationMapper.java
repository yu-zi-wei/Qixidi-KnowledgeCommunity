package com.qixidi.business.mapper.user;

import com.qixidi.business.domain.entity.user.UserInformation;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInformationMapper extends BaseMapperPlus<UserInformationMapper, UserInformation, UserInformation> {
}
