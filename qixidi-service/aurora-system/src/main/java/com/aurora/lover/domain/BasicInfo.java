/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 10:23
 * @version 1.0
 */
package com.aurora.lover.domain;

import com.aurora.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 网站基本信息对象 basic_info
 *
 * @author ruoyi
 * @date 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("basic_info")
public class BasicInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 网站标题
     */
    private String title;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 左头像
     */
    private String leftImg;

    private String leftImgId;

    private String rightImgId;

    private String type;

    private Long state;
    /**
     * 左名称
     */
    private String leftName;
    /**
     * 右头像
     */
    private String rightImg;
    /**
     * 右名称
     */
    private String rightName;

    private Date createTime;

    private Date updateTime;

    /**
     * 恋爱时间前缀
     */
    private String loverPrefix;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loverTime;

}
