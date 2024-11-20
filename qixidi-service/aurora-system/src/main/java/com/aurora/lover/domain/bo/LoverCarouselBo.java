/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:13
 * @version 1.0
 */
package com.aurora.lover.domain.bo;
import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 爱情轮播图业务对象 lover_carousel
 *
 * @author ruoyi
 * @date 2022-11-29
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LoverCarouselBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 图片
     */
    @NotBlank(message = "图片不能为空", groups = {AddGroup.class, EditGroup.class})
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
    @NotBlank(message = "类型不能为空", groups = { EditGroup.class })
    private String type;

    /**
     * 状态
     */
    private Long state;


}
