package com.aurora.business.domain.bo.user;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;


/**
 * 用户签到业务对象 user_report
 *
 * @author aurora
 * @date 2023-04-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("用户签到业务对象")
public class UserReportBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * uid
     */
    @ApiModelProperty(value = "uid", required = true)
    private String uid;

    /**
     * 签到时间
     */
    @ApiModelProperty(value = "签到时间", required = true)
    @NotNull(message = "签到时间不能为空", groups = {AddGroup.class, EditGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;

    /**
     * 获得a币数量
     */
    @ApiModelProperty(value = "获得a币数量", required = true)
    @NotNull(message = "获得a币数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long aCurrency;


}
