/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 16:49
 * @version 1.0
 */
package com.qixidi.love.domain;

import com.light.core.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 爱情记录对象 lover_record
 *
 * @author ziwei
 * @date 2022-11-21
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lover_record")
public class LoverRecord extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;

    /**
     * 状态（0：正常，1：已删除）
     */
    private Long state;
    /**
     * 地址
     */
    private String address;

    private Date createTime;

    private Date updateTime;
}
