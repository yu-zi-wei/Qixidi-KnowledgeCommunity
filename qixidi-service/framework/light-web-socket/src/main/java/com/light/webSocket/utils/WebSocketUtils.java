package com.light.webSocket.utils;

import com.light.core.utils.JsonUtils;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 工具类
 *
 * @author zi-wei
 * @create 2025/2/27 14:55
 */
public class WebSocketUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtils.class);
    //存储已连接的客户端信息
    private static ConcurrentHashMap<String, Session> websocketMap = new ConcurrentHashMap<>();

    /**
     * 建立链接
     *
     * @param key
     * @param session
     */
    public static void addLinks(String key, Session session) {
        websocketMap.put(key, session);
        logger.info("用户创建连接：{}，当前在线链接数:{}", key, websocketMap.size());
    }

    /**
     * 移除指定连接
     *
     * @param key
     */
    public static void removeLinks(String key) {
        if (websocketMap.containsKey(key)) {
            websocketMap.remove(key);
            logger.info("用户关闭：{}，人数:{}", key, websocketMap.size());
        }
    }

    /**
     * 获取所有客户端
     *
     * @return
     */
    public static ConcurrentHashMap<String, Session> getAllSession() {
        return websocketMap;
    }

    /**
     * 判断客户端是否在线
     *
     * @param key
     * @return
     */
    public static Boolean containsKey(String key) {
        return websocketMap.containsKey(key);
    }

    /**
     * 获取指定客户端 Session
     *
     * @param key
     * @return
     */
    public static Session getSession(String key) {
        return websocketMap.get(key);
    }

    /**
     * 向指定客户端发送消息
     *
     * @param session
     * @param message Object
     */
    public static void sendMessage(Session session, Object message) {
        try {
            if (session != null && message != null) {
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
     * @param key
     * @param message Object
     */
    public static void sendMessage(String key, Object message) {
        Session session = websocketMap.get(key);
        try {
            if (session != null && message != null) {
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
     * @param key
     * @param message String
     */
    public static void sendMessage(String key, String message) {
        // 获取该 userId 对应的 SocketDomain 对象
        Session session = websocketMap.get(key);
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
    private static void sendMessageToAllExpectSelf(String message, Session fromSession) {
        for (Map.Entry<String, Session> client : websocketMap.entrySet()) {
            Session toSeesion = client.getValue();
            if (!toSeesion.getId().equals(fromSession.getId()) && toSeesion.isOpen()) {
                toSeesion.getAsyncRemote().sendText(message);
                logger.info("服务端发送消息给：{} 内容：{}", client.getKey(), message);
            }
        }
    }

    /**
     * 给包括当前客户端的全部客户端发送消息
     *
     * @param message
     */
    private static void sendMessageToAll(String message) {
        for (Map.Entry<String, Session> client : websocketMap.entrySet()) {
            Session toSeesion = client.getValue();
            if (toSeesion.isOpen()) {
                toSeesion.getAsyncRemote().sendText(message);
                logger.info("服务端发送消息给：{} 内容：{}", client.getKey(), message);
            }
        }
    }

    /**
     * 给外部调用的方法接口（群发消息）
     *
     * @param Message
     */
    public static void sendAll(String Message) {
        sendMessageToAll(Message);
    }


}
