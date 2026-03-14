package com.light.mybatisPlus.interfaces;

import com.light.mybatisPlus.domain.dto.UserInfoIdNameDto;

/**
 * @author zi-wei
 * @create 2025/2/19 10:57
 */
public interface UserInfoInterface {

    UserInfoIdNameDto getLoginUser();

    UserInfoIdNameDto getTripartiteUser();
}
