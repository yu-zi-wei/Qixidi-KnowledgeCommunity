package com.light.mybatisPlus.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zi-wei
 * @create 2025/2/19 10:59
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoIdNameDto {

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户名称
     */
    private String username;
}
