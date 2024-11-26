package com.aurora.common.enums;

public enum MsgEnums {
    /**
     * 评论
     */
    COMMENT_DELETED(1001, "评论已删除"),

    /**
     * 文章
     */
    ARTICLE_DELETED(2001, "文章已删除"),

    /**
     * 异常
     */
    DELETE_ERROR(3001, "删除异常,请联系管理员"),
    /**
     * 专栏
     */
    SPECIAL_CONDITION_ERROR(4001, "该专栏存在文章,无法删除"),
    /**
     * 验证
     */
    CAPTCHA_ALREADY_EXISTS(5001, "短信发送太频繁,请稍后再试"),
    VERIFICATION_CODE_ERROR(5002, "验证码错误"),
    VERIFICATION_CODE_EXPIRED(5003, "验证码已过期"),
    VERIFICATION_CODE_UNDERCOUNT(5004, "今日验证码次数已达上限"),
    ;
    private Integer code;
    private String value;

    MsgEnums(Integer code, String value) {
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
