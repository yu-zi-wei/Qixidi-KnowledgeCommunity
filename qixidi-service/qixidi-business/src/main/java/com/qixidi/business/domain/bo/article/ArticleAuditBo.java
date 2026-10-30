package com.qixidi.business.domain.bo.article;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 文章审核业务对象（后台终审：通过/拒绝）——独立于编辑接口，不携带文章内容字段
 *
 * @author aurora
 * @date 2026-10-30
 */
@Data
public class ArticleAuditBo {

    /**
     * 文章id列表（支持批量审核）
     */
    @NotEmpty(message = "请选择文章")
    private List<Long> ids;

    /**
     * 审核结果：2 通过 / 3 拒绝（ArticleAuditStateEnums）
     */
    @NotNull(message = "请选择审核结果")
    private Integer auditState;
}
