package com.aurora.business.selector.webSocket;

import com.aurora.common.enums.WebSocketEnum;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
public interface WebSocketInterface {
    boolean support(WebSocketEnum anEnum);

    void execute(String uuid, WebSocketEnum anEnum);
}
