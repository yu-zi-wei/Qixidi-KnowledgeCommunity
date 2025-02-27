package com.light.webSocket.selector;


import com.light.webSocket.domain.enums.WebSocketEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ziwei
 * @date 2024年01月07日
 * WebSocket消息发送中转站
 */
@Component
public class WebSocketSelector {
    private static Map<String, WebSocketInterface> taskClearHandlers;

    @Autowired
    private WebSocketSelector(ApplicationContext applicationContext) {
        this.taskClearHandlers = applicationContext.getBeansOfType(WebSocketInterface.class);
    }

    private WebSocketSelector() {
    }

    public static void execute(String roleid, WebSocketEnum anEnum) {
        for (Map.Entry<String, WebSocketInterface> entry : taskClearHandlers.entrySet()) {
            WebSocketInterface handler = entry.getValue();
            if (handler.support(anEnum)) handler.execute(roleid, anEnum);
        }
    }
}
