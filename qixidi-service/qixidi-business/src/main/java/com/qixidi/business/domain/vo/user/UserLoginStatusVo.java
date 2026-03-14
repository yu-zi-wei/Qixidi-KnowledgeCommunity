package com.qixidi.business.domain.vo.user;

import com.qixidi.auth.domain.entity.TripartiteUser;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录状态vo
 *
 * @author zi-wei
 * @create 2025/7/24 9:33
 */
@Data
@NoArgsConstructor
public class UserLoginStatusVo {
    /**
     * 用户第三方系统的唯一id
     */
    private String uuid;

    /**
     * token
     */
    private String token;

    /**
     * 是否登录
     */
    private Boolean isLogin = false;

    /**
     * 是否为前台
     */
    private Boolean isBackstage = false;
    /**
     * 用户信息
     */
    private TripartiteUser tripartiteUser;

}
