/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:19
 * @version 1.0
 */
package com.aurora.lover.domain;

import com.aurora.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 关于我们对象 about
 *
 * @author ruoyi
 * @date 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("about")
public class About extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 状态
     */
    private Long state;

}
