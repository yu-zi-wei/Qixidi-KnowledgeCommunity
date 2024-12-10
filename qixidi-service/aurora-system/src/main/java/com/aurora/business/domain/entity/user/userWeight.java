package com.aurora.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 前台权重对象
 */
@Data
@NoArgsConstructor
@TableName("user_weight")
@Accessors(chain = true)
public class userWeight {

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
     * 目标id
     */
    private Integer targetId;

    /**
     * 类型（1：标签，2：分类）
     */
    private Integer type;

    /**
     * 计算时间
     */
    private Date createTime;

}
