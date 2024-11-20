/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月20日 16:47
 * @version 1.0
 */
package com.aurora.lover.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 时光相册对象 album
 *
 * @author ruoyi
 * @date 2022-11-20
 */
@Data
@TableName("album")
public class Album {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 图片链接
     */
    private String img;
    /**
     * ossId
     */
    private String imgId;
    /**
     * 地址
     */
    private String address;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 顺序
     */
    @TableField("`order`")
    private Long order;
    /**
     * 状态（0，正常，1，已删除）
     */
    private Long state;

    private Date updateTime;

    private Date createTime;
}
