package com.qixidi.business.domain.enums;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
public enum WebSocketEnum {
    INSIDE_NOTICE(1,"站内通知"),
    SYSTEM_TASKS(2,"系统任务"),
    INSIDE_CHAT(3,"站内聊天"),
    PERSONAL_RED_DOT(4,"私信红点"),
        ;
    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    WebSocketEnum() {
    }

    WebSocketEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
