package com.aurora.business.domain.bo;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 搜索记录业务对象 b_search_records
 *
 * @author aurora
 * @date 2023-04-18
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("搜索记录业务对象")
public class SearchRecordsBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 内容
     */
    @ApiModelProperty(value = "内容", required = true)
    @NotBlank(message = "内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;


}

