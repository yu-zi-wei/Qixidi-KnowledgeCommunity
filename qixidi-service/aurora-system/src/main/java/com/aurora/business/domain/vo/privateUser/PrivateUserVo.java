package com.aurora.business.domain.vo.privateUser;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 私信记录视图对象 b_private_user
 *
 * @author aurora
 * @date 2023-03-23
 */
@Data
@ApiModel("私信记录视图对象")
@ExcelIgnoreUnannotated
public class PrivateUserVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    @ApiModelProperty("id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    @ApiModelProperty("用户id")
    private String uid;

    /**
     * 目标用户id
     */
    @ExcelProperty(value = "目标用户id")
    @ApiModelProperty("目标用户id")
    private String targetUid;

    /**
     * 最后一条聊天数据
     */
    private String lastNews;

    /**
     * 未读消息条数
     */
    private Integer unreadCount = 0;

    /**
     * 目标用户名称
     */
    private String targetName;

    /**
     * 目标用户头像
     */
    private String targetAvatar;

    /**
     * 职业
     */
    private String targetOccupation;


    private Date createTime;

    private Date updateTime;
}

