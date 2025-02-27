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
@ServerEndpoint(value = "/websocket/{userId}/{type}")
public class WebSocketUserApi {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketUserApi.class);
    //当前客户端名称
    private String key = "";

    /**
     * 建立链接
     *
     * @param session
     * @param userId
     * @param type
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("type") Integer type) {
        this.key = userId;
        //建立链接
        WebSocketUtils.addLinks(userId, session);
        try {
            if (type == 1) {
                WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(userId);
            } else if (type == 4) {
                WebSocketSelector.execute(WebSocketEnum.PERSONAL_RED_DOT).execute(userId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        }
    }

}
