/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 14:51
 * @version 1.0
 */
package com.qixidi.love.domain.bo;


import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;

/**
 * 网站基本信息业务对象 basic_info
 *
 * @author ziwei
 * @date 2022-11-21
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class BasicInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 网站标题
     */
    private String title;

    /**
     * 介绍
     */
    private String introduce;

    /**
     * 左头像
     */
    private String leftImg;

    private String leftImgId;

    private String rightImgId;

    @NotBlank(message = "类型不能为空", groups = {EditGroup.class})
    private String type;

    private Long state;

    /**
     * 恋爱时间前缀
     */
    private String loverPrefix;

    /**
     * 左名称
     */
    private String leftName;

    /**
     * 右头像
     */
    private String rightImg;

    /**
     * 右名称
     */
    private String rightName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @NotNull(message = "恋爱时间不能为空", groups = {AddGroup.class, EditGroup.class})
    private Date loverTime;

}

