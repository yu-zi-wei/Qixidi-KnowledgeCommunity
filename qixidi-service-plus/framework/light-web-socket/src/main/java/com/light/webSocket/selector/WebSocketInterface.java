package com.light.webSocket.selector;


import com.light.webSocket.domain.enums.WebSocketEnum;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
public interface WebSocketInterface {
    boolean support(WebSocketEnum anEnum);

    void execute(String uuid, WebSocketEnum anEnum);
}
