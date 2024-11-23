/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月22日 15:53
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
 * 爱情清单业务对象 repertoire
 *
 * @author ziwei
 * @date 2022-11-22
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class RepertoireBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 清单名称
     */
    @NotBlank(message = "清单名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String name;

    /**
     * 清单内容
     */
    private String content;

    /**
     * 清单图片
     */
    private String img;

    /**
     * 状态（0，正常，1，已删除）
     */
    @NotNull(message = "状态（0，正常，1，已删除）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long state;

    /**
     * 排序
     */
    private Long order;

    /**
     * 地址
     */
    private String address;


}

