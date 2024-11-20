/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:55
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
 * 爱情树对象 lover_tree
 *
 * @author ruoyi
 * @date 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lover_tree")
public class LoverTree extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 爱情誓言
     */
    private String content;
    /**
     * 恋爱时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loverTime;
    /**
     * 恋爱时间前缀
     */
    private String loverPrefix;

    /**
     * 状态
     */
    private Long state;
    /**
     * 类型
     */
    private String type;

}
