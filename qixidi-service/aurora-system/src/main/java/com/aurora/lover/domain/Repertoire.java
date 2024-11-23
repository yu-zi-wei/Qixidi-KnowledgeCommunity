/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月22日 15:51
 * @version 1.0
 */
package com.aurora.lover.domain;

import com.aurora.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 爱情清单对象 repertoire
 *
 * @author ziwei
 * @date 2022-11-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("repertoire")
public class Repertoire extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 清单名称
     */
    private String name;
    /**
     * 清单内容
     */
    private String content;
    /**
     * 清单图片
     */
    private String img;
    /**
     * 状态（0，正常，1，已删除）
     */
    private Long state;
    /**
     * 排序
     */
    @TableField("`order`")
    private Long order;

    private Integer isComplete;
    /**
     * 地址
     */
    private String address;

    private Date createTime;

    private Date updateTime;

}
