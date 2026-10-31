package com.qixidi.business.domain.bo.user;

import com.light.core.core.domain.PageQuery;
import lombok.Data;

/**
 * 前台登录日志查询条件
 *
 * @author ziwei
 * @date 2026-10-31
 */
@Data
public class SignLogBo extends PageQuery {

    /**
     * 用户名称（模糊）
     */
    private String userName;

    /**
     * 登录 IP（模糊）
     */
    private String ipAddress;

    /**
     * 日志类型：0 登录 1 登出
     */
    private Integer type;

}
