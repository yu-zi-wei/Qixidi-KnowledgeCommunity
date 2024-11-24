package com.aurora.business.domain.entity.dictum;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 名言分组对象 b_dictum_group
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@TableName("b_dictum_group")
@Accessors(chain = true)
public class DictumGroup {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id")
    private Long id;
    ;
    /**
     * 分组名称
     */
    private String name;
    /**
     * 封面
     */
    private String cover;
    /**
     * 简介
     */
    private String briefIntroduction;
    /**
     * 收录数
     */
    private Long employSum;
    /**
     * 状态（0：正常吗：已删除）
     */
    private Integer state;

    private Date createTime;

    private Date updateTime;

}

