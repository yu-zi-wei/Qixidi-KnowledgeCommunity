package com.light.webSocket.selector;


import com.light.webSocket.domain.enums.WebSocketEnum;
import jakarta.websocket.Session;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
public interface WebSocketInterface {
    boolean support(WebSocketEnum anEnum);

    /**
     * 通用实现
     *
     * @param uuid
     */
    void execute(String uuid);

    /**
     * ai对话实现
     *
     * @param content
     * @param session
     */
    default void aiExecute(String content, Session session) {

    }
}
