/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:29
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
 * 爱情祝福语业务对象 lover_comment
 *
 * @author ziwei
 * @date 2022-11-30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LoverCommentBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 评论
     */
    @NotBlank(message = "评论不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;


}
