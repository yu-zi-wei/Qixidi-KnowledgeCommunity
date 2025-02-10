package com.qixidi.business.domain.bo;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;


/**
 * 搜索记录业务对象 b_search_records
 *
 * @author aurora
 * @date 2023-04-18
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchRecordsBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;


}

