/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:20
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
 * 关于我们业务对象 about
 *
 * @author ziwei
 * @date 2022-11-30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AboutBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = {AddGroup.class})
    private String content;

    /**
     * 状态
     */
    private Long state;


}
