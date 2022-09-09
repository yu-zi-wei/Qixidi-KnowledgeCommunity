package com.aurora.business.domain.entity.special;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


/**
 * 专栏信息对象 special_information
 *
 * @author aurora
 * @date 2022-08-19
 */
@Data
@TableName("special_information")
public class SpecialInformation {

    private static final long serialVersionUID = 1L;

    /**
     * 专栏id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 专栏名称
     */
    private String specialName;
    /**
     * 专栏简介
     */
    private String specialIntroduce;
    /**
     * 专栏状态（0：正常，1：未启用）
     */
    private Integer status;
    /**
     * 用户id
     */
    private Long uid;
    /**
     * 修改者id
     */
    private Long updateId;

    private Date createTime;

    private Date updateTime;

}
