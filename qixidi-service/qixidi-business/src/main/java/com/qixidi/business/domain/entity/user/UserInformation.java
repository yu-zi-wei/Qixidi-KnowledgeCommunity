package com.qixidi.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@TableName("b_user_information")
@Accessors(chain = true)
public class UserInformation {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private String uuid;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 个人主页地址
     */
    private String homepage;
    /**
     * 个人简介
     */
    private String introduce;
    /**
     * 用户经验值
     */
    private Long empirical;
    /**
     * 用户等级
     */
    private String grade;
    /**
     * 用户状态（0：正常，1：已删除，2：冻结，）
     */
    private Integer userState;

    private Date createTime;
    /**
     * 更新者id
     */
    private String updateId;
}
