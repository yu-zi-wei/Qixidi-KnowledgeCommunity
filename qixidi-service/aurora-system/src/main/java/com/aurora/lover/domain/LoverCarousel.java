/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:07
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
 * 爱情轮播图对象 lover_carousel
 *
 * @author ziwei
 * @date 2022-11-29
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lover_carousel")
public class LoverCarousel extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id",type=IdType.AUTO)
    private Long id;
    /**
     * 图片
     */
    private String img;
    /**
     * 图片id
     */
    private String imgId;
    /**
     * 简介
     */
    private String introduce;
    /**
     * 类型
     */
    private String type;
    /**
     * 状态
     */
    private Long state;

}
