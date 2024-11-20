package com.aurora.business.mapper.user;

import com.aurora.business.domain.entity.user.UserInformation;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInformationMapper extends BaseMapperPlus<UserInformationMapper, UserInformation, UserInformation> {
}
