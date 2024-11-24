package com.aurora.business.domain.bo.news;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 系统消息业务对象 b_news_system_info
 *
 * @author aurora
 * @date 2023-04-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("系统消息业务对象")
public class NewsSystemInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 消息标题
     */
    @ApiModelProperty(value = "消息标题", required = true)
    @NotBlank(message = "消息标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newsTitle;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容", required = true)
    @NotBlank(message = "消息内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newsContent;

    /**
     * 是否有详情（1：无，2：有）
     */
    @ApiModelProperty(value = "是否有详情（1：无，2：有）", required = true)
    @NotNull(message = "是否有详情（1：无，2：有）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long isDetails;

    /**
     * 状态
     */
    private Long state;
    /**
     * 是否群发（1：群发，2：指定人发）
     */
    private Long isMassAir;
    /**
     * uid
     */
    private String uid;

    /**
     * 消息类型（1：系统，2：用户）
     */
    @ApiModelProperty(value = "消息类型（1：系统，2：用户）", required = true)
    @NotNull(message = "消息类型（1：系统，2：用户）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long type;

    /**
     * 失效时间
     */
    @ApiModelProperty(value = "失效时间", required = true)
    private Date lapseTime;


}

