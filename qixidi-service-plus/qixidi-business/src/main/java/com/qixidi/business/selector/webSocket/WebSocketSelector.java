package com.qixidi.business.selector.webSocket;


import com.light.core.enums.WebSocketEnum;
import com.light.core.utils.context.SpringContext;

import java.util.Map;

/**
 * @author ziwei
 * @date 2024年01月07日
 * WebSocket消息发送中转站
 */
public class WebSocketSelector {
    private static Map<String, WebSocketInterface> taskClearHandlers = SpringContext.inst().getInterfaceBeans(WebSocketInterface.class);

    private WebSocketSelector() {
    }

    public static void execute(String roleid, WebSocketEnum anEnum) {
        for (Map.Entry<String, WebSocketInterface> entry : taskClearHandlers.entrySet()) {
            WebSocketInterface handler = entry.getValue();
            if (handler.support(anEnum)) handler.execute(roleid, anEnum);
        }
    }
}
