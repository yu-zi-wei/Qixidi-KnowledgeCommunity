package com.qixidi.business.domain.bo.user;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;
import java.util.Date;


/**
 * 用户签到业务对象 b_user_report
 *
 * @author aurora
 * @date 2023-04-10
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserReportBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * uid
     */
    private String uid;

    /**
     * 签到时间
     */
    @NotNull(message = "签到时间不能为空", groups = {AddGroup.class, EditGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date reportTime;

    /**
     * 获得a币数量
     */
    @NotNull(message = "获得a币数量不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long aCurrency;


}
