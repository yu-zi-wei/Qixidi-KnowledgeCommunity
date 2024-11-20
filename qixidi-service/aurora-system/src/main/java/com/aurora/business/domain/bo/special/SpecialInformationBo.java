package com.aurora.business.domain.bo.special;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 专栏信息业务对象 special_information
 *
 * @author aurora
 * @date 2022-08-19
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("专栏信息业务对象")
public class SpecialInformationBo extends BaseEntity {

    /**
     * 专栏id
     */
    @ApiModelProperty(value = "专栏id", required = true)
    private Long id;

    /**
     * 专栏名称
     */
    @ApiModelProperty(value = "专栏名称", required = true)
    @NotBlank(message = "专栏名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String specialName;

    /**
     * 专栏简介
     */
    @ApiModelProperty(value = "专栏简介", required = true)
    @NotBlank(message = "专栏简介不能为空", groups = {AddGroup.class, EditGroup.class})
    private String specialIntroduce;
    /**
     * 封面
     */
    @ApiModelProperty("封面")
    private String cover;
    /**
     * 专栏状态（0：正常，1：未启用）
     */
    @ApiModelProperty(value = "专栏状态（0：正常，1：未启用）", required = true)
    private Integer state;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", required = true)
    private String username;

    /**
     * 修改者id
     */
    @ApiModelProperty(value = "修改者id", required = true)
    private String updateId;

    /**
     * 收录数
     */
    private Integer includedCount;


}

