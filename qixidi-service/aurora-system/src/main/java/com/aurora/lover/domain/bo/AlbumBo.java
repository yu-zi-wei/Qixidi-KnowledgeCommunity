/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月20日 16:50
 * @version 1.0
 */
package com.aurora.lover.domain.bo;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * 时光相册业务对象 album
 *
 * @author ziwei
 * @date 2022-11-20
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AlbumBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 图片链接
     */
    @NotBlank(message = "图片链接不能为空", groups = {AddGroup.class, EditGroup.class})
    private String img;

    @NotBlank(message = "图片id不能为空", groups = {AddGroup.class, EditGroup.class})
    private String imgId;

    /**
     * 地址
     */
    private String address;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 顺序
     */
    private Long order;

    /**
     * 状态（0，正常，1，已删除）
     */
    private Long state;


}
