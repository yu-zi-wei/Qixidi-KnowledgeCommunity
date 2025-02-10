package com.qixidi.business.domain.bo;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;


/**
 * 用户反馈业务对象 Feedback
 *
 * @author aurora
 * @date 2023-04-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class FeedbackBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户名称
     */
    private String nickname;

    /**
     * 反馈内容
     */
    private String FeedbackContent;

    /**
     * 反馈标题
     */
    @NotBlank(message = "标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String FeedbackTitle;

    /**
     * 状态
     */
    private Integer status;


}

