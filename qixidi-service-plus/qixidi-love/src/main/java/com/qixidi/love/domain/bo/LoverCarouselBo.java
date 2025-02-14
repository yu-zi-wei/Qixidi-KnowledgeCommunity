/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:13
 * @version 1.0
 */
package com.qixidi.love.domain.bo;
import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 爱情轮播图业务对象 lover_carousel
 *
 * @author ziwei
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
