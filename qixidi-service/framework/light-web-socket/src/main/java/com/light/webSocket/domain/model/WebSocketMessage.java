package com.light.webSocket.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket 消息包装类
 *
 * @param <T> 消息数据类型
 * @author zi-wei
 */
@Data
@NoArgsConstructor
public class WebSocketMessage<T> {

    /**
     * 消息类型
     */
    private int type;

    /**
     * 消息数据
     */
    private T data;

    public WebSocketMessage(int type, T data) {
        this.type = type;
        this.data = data;
    }
}
