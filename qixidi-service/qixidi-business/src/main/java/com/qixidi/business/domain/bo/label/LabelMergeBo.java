package com.qixidi.business.domain.bo.label;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 标签/标签分组合并业务对象（源合并入目标，源被删除）
 *
 * @author ziwei
 */
@Data
public class LabelMergeBo {

    /**
     * 源 id（合并后删除）
     */
    @NotNull(message = "源 id 不能为空")
    private Long sourceId;

    /**
     * 目标 id（合并后保留）
     */
    @NotNull(message = "目标 id 不能为空")
    private Long targetId;
}
