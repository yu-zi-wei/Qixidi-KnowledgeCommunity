package com.aurora.business.domain.bo.topic;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

/**
 * 话题信息业务对象 topic_infor
 *
 * @author aurora
 * @date 2023-03-09
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("话题信息业务对象")
public class TopicInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 封面
     */
    @ApiModelProperty(value = "封面", required = true)
    @NotBlank(message = "封面不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cover;

    /**
     * 话题名称
     */
    @ApiModelProperty(value = "话题名称", required = true)
    @NotBlank(message = "话题名称不能为空", groups = { AddGroup.class, EditGroup.class })
    private String topicName;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述", required = true)
    @NotBlank(message = "描述不能为空", groups = { AddGroup.class, EditGroup.class })
    private String describe;

    /**
     * 顺序
     */
    @ApiModelProperty(value = "顺序", required = true)
    private Long order;

    /**
     * 状态（0：开启，1：已结束）
     */
    @ApiModelProperty(value = "状态（0：开启，1：已结束）", required = true)
    private Long state;

    /**
     * 参与人数
     */
    @ApiModelProperty(value = "参与人数", required = true)
    private Long number;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date endTime;


}
