package com.qixidi.business.domain.bo.configure;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;


/**
 * 网站文档业务对象 to_site_file
 *
 * @author aurora
 * @date 2022-10-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ToSiteFileBo extends BaseEntity {

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
     * md内容
     */
    @NotBlank(message = "内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    /**
     * 状态（0：正常，1：已失效）
     */
    private Integer state;

    /**
     * 类型
     */
    private String type;

    /**
     * 主题
     */
    private String theme;
}

