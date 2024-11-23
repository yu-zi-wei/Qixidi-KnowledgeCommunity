/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 16:51
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
 * 爱情记录业务对象 lover_record
 *
 * @author ziwei
 * @date 2022-11-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LoverRecordBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = {AddGroup.class})
    private String content;

    /**
     * 状态（0：正常，1：已删除）
     */

    private Long state;

    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String address;


}
