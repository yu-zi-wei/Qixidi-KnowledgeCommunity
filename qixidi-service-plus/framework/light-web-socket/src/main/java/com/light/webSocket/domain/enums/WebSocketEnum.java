package com.light.webSocket.domain.enums;

import lombok.Getter;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
@Getter
public enum WebSocketEnum {
    INSIDE_NOTICE(1, "站内通知"),
    SYSTEM_TASKS(2, "系统任务"),
    INSIDE_CHAT(3, "站内聊天"),
    PERSONAL_RED_DOT(4, "私信红点"),
    AI(5, "AI对话"),
    ;
    private int code;
    private String msg;

    WebSocketEnum() {
    }

    WebSocketEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
