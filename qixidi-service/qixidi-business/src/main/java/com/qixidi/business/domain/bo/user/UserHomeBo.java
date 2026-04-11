package com.qixidi.business.domain.bo.user;

import com.light.core.core.domain.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserHomeBo extends BaseEntity {

    @NotBlank(message = "uid不能为空")
    private String uid;
}
