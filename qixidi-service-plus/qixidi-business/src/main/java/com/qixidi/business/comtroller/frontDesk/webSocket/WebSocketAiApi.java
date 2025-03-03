package com.qixidi.business.comtroller.frontDesk.webSocket;

import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.light.webSocket.utils.WebSocketUtils;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author ziwei
 * @date 2024年01月05日
 */
@Component
@ServerEndpoint(value = "/websocket/ai/{userId}")
public class WebSocketAiApi {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketAiApi.class);
    //当前客户端名称
    private String key = "";

    /**
     * 建立链接
     *
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.key = userId;
        //建立链接
        WebSocketUtils.addLinks(userId, session);
    }

    /**
     * 关闭链接
     */
    @OnClose
    public void onClose() {
        WebSocketUtils.removeLinks(key);
    }

    /**
     * 服务端接收到消息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        if (StringUtil.isNotEmpty(message)) {
            logger.info("收到用户消息:{},报文:{}", key, message);
            WebSocketSelector.execute(WebSocketEnum.AI).aiExecute(message, session);
        }
    }
}
