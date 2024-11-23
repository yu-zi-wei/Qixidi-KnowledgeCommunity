/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:57
 * @version 1.0
 */
package com.aurora.lover.domain.bo;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 爱情树业务对象 lover_tree
 *
 * @author ziwei
 * @date 2022-11-30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LoverTreeBo extends BaseEntity {

    /**
     * id
     */
    @NotNull(message = "id不能为空", groups = {EditGroup.class})
    private Long id;

    /**
     * 爱情誓言
     */
    @NotBlank(message = "爱情誓言不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    /**
     * 恋爱时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "恋爱时间不能为空", groups = {AddGroup.class, EditGroup.class})
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
    @NotBlank(message = "类型不能为空", groups = {EditGroup.class})
    private String type;


}
