package com.qixidi.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 前台登录日志对象 b_sign_log
 *
 * @author ziwei
 * @date 2026-10-31
 */
@Data
@TableName("b_sign_log")
public class SignLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 登录地址（IP 归属地）
     */
    private String location;

    /**
     * 时间
     */
    private Date createTime;

    /**
     * 类型（0：登录，1：登出）
     */
    private Integer type;

}
