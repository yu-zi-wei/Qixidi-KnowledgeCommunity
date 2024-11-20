package com.aurora.business.domain.bo;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;


/**
 * 用户反馈业务对象 feedback
 *
 * @author aurora
 * @date 2023-04-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户反馈业务对象")
public class FeedbackBo extends BaseEntity {

    /**
     *
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称", required = true)
    private String nickname;

    /**
     * 反馈内容
     */
    @ApiModelProperty(value = "反馈内容", required = true)
    private String feedbackContent;

    /**
     * 反馈标题
     */
    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String feedbackTitle;

    /**
     * 状态
     */
    private Integer status;


}

