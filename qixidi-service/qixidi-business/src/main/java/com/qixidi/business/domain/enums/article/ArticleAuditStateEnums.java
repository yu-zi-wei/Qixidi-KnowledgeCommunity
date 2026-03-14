package com.qixidi.business.domain.enums.article;

/**
 * 文章审核状态
 */
public enum ArticleAuditStateEnums {
    UNDER_REVIEW(1, "审核中"),
    APPROV(2, "审核通过"),
    FAILED_TO_PASS_REVIEW(3, "审核不通过"),
    DRAFT(4, "草稿"),
    ;

    private Integer code;
    private String value;

    ArticleAuditStateEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

