package com.qixidi.business.service.webSocket;

import com.light.core.utils.JsonUtils;
import com.qixidi.business.domain.enums.WebSocketEnum;
import com.qixidi.business.selector.webSocket.WebSocketSelector;
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ziwei
 * @date 2024年01月05日
 */
@Component
@ServerEndpoint(value = "/websocket/{userId}/{type}")
public class WebSocketServer {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    //Map用来存储已连接的客户端信息
    private static ConcurrentHashMap<String, Session> websocketMap = new ConcurrentHashMap<>();
    //当前客户端名称
    private String userId = "";

    /**
     * 用户是否在线
     *
     * @param userId
     * @return
     */
    public Boolean isOnline(String userId) {
        return websocketMap.containsKey(userId);
    }

    /**
     * 用户下线
     *
     * @param userId
     */
    public void userLogout(String userId) {
        //用户链接
        if (websocketMap.containsKey(userId)) {
            websocketMap.remove(userId);
            logger.info("用户下线：{}，人数:{}", userId, websocketMap.size());
        }
        //优化私信链接
        String uid1 = userId + ":sx";
        if (websocketMap.containsKey(uid1)) {
            websocketMap.remove(uid1);
            logger.info("用户下线：{}，人数:{}", uid1, websocketMap.size());
        }
    }

    /**
     * 建立链接
     *
     * @param session
     * @param userId
     * @param type
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId, @PathParam("type") Integer type) {
        this.userId = userId;
        websocketMap.put(userId, session);
        logger.info("用户创建连接：{}，type：{}，当前在线链接数:{}", userId, type, websocketMap.size());
        try {
            if (type == 1) {
                WebSocketSelector.execute(userId, WebSocketEnum.INSIDE_NOTICE);
            } else if (type == 4) {
                WebSocketSelector.execute(userId, WebSocketEnum.PERSONAL_RED_DOT);
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
        if (websocketMap.containsKey(userId)) {
            websocketMap.remove(userId);
            logger.info("用户关闭：{}，人数:{}", userId, websocketMap.size());
        }
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
            logger.info("收到用户消息:{},报文:{}", userId, message);
        }
    }

    /**
     * 向指定客户端发送消息
     *
     * @param userId
     * @param message Object
     */
    public void sendMessageToUser(String userId, Object message) {
        // 获取该 userId 对应的 SocketDomain 对象
        Session session = websocketMap.get(userId);
        try {
            if (session != null) {
                // 向客户端发送消息
                session.getAsyncRemote().sendText(JsonUtils.toJsonString(message));
//                session.getAsyncRemote().sendObject(message);// TODO:客户端接收失败，待查
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 向指定客户端发送消息
     *
     * @param userId
     * @param message String
     */
    public void sendMessageToUser(String userId, String message) {
        // 获取该 userId 对应的 SocketDomain 对象
        Session session = websocketMap.get(userId);
        try {
            if (session != null) {
                // 向客户端发送消息
                session.getAsyncRemote().sendText(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 给除了当前客户端的其他客户端发消息
     *
     * @param message
     * @param fromSession
     */
    private void sendMessageToAllExpectSelf(String message, Session fromSession) {
        for (Map.Entry<String, Session> client : websocketMap.entrySet()) {
            Session toSeesion = client.getValue();
            if (!toSeesion.getId().equals(fromSession.getId()) && toSeesion.isOpen()) {
                toSeesion.getAsyncRemote().sendText(message);
                logger.info("服务端发送消息给{}:{}", client.getKey(), message);
            }
        }
    }

    /**
     * 给包括当前客户端的全部客户端发送消息
     *
     * @param message
     */
    private void sendMessageToAll(String message) {
        for (Map.Entry<String, Session> client : websocketMap.entrySet()) {
            Session toSeesion = client.getValue();
            if (toSeesion.isOpen()) {
                toSeesion.getAsyncRemote().sendText(message);
                logger.info("服务端发送消息给{}:{}", client.getKey(), message);
            }
        }
    }

    /**
     * 给外部调用的方法接口（群发消息）
     *
     * @param Message
     */
    public void sendAll(String Message) {
        sendMessageToAll(Message);
    }

}
