package com.aurora.business.domain.entity.shield;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 屏蔽词库对象 to_shield_word
 *
 * @author aurora
 * @date 2023-02-20
 */
@Data
@TableName("to_shield_word")
public class ToShieldWord {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 屏蔽字
     */
    private String keyword;
    /**
     * 类型（1：文章，2：评论）
     */
    private Long type;
    /**
     * 屏蔽词权重
     */
    private Long weight;
    /**
     * 状态（0：正常，1：已失效）
     */
    private Long state;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 创建名
     */
    private String createBy;

}
