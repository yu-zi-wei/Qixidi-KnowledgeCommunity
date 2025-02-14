package com.qixidi.love.domain.bo;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;

/**
 * 基本配置业务对象 lover_congfig
 *
 * @author ziwei
 * @date 2022-12-02
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class LoverConfigBo extends BaseEntity {

    /**
     * is
     */
    private Long id;

    /**
     * 邮箱（多个以逗号隔开）
     */
    @NotBlank(message = "邮箱（多个以逗号隔开）不能为空", groups = {AddGroup.class, EditGroup.class})
    private String mail;

    /**
     * 备案号
     */
    private String filings;

    /**
     * 域名
     */
    private String realmName;


}

