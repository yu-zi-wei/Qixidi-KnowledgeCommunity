package com.qixidi.business.domain.bo.special;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;


/**
 * 专栏信息业务对象 b_special_information
 *
 * @author aurora
 * @date 2022-08-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SpecialInformationBo extends BaseEntity {

    /**
     * 专栏id
     */
    private Long id;

    /**
     * 专栏名称
     */
    @NotBlank(message = "专栏名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String specialName;

    /**
     * 专栏简介
     */
    @NotBlank(message = "专栏简介不能为空", groups = {AddGroup.class, EditGroup.class})
    private String specialIntroduce;
    /**
     * 封面
     */
    private String cover;
    /**
     * 专栏状态（0：正常，1：未启用）
     */
    private Integer state;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 修改者id
     */
    private String updateId;

    /**
     * 收录数
     */
    private Integer includedCount;


}

