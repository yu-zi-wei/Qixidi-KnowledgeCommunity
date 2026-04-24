package com.light.webSocket.utils;

import com.light.core.utils.JsonUtils;
import com.light.webSocket.domain.model.WebSocketMessage;
import jakarta.websocket.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * WebSocket 工具类
 * <p>
 * 支持同一用户多个标签页同时在线（key → List<Session>）
 *
 * @author zi-wei
 * @create 2025/2/27 14:55
 */
public class WebSocketUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketUtils.class);
    // 存储 key → 多个 Session（同一用户多标签页）
    private static ConcurrentHashMap<String, CopyOnWriteArrayList<Session>> websocketMap = new ConcurrentHashMap<>();

    /**
     * 建立链接
     */
    public static void addLinks(String key, Session session) {
        websocketMap.computeIfAbsent(key, k -> new CopyOnWriteArrayList<>()).add(session);
//        logger.info("用户创建连接：{}，sessionId:{}，当前该key连接数:{}，总连接数:{}", key, session.getId(), websocketMap.get(key).size(), websocketMap.size());
    }

    /**
     * 移除指定连接
     */
    public static void removeLinks(String key, Session session) {
        CopyOnWriteArrayList<Session> sessions = websocketMap.get(key);
        if (sessions != null) {
            sessions.remove(session);
            if (sessions.isEmpty()) {
                websocketMap.remove(key);
            }
//            logger.info("用户关闭连接：{}，sessionId:{}，剩余连接数:{}", key, session.getId(), sessions.size());
        }
    }

    /**
     * 移除指定 key 的所有连接（兼容旧调用方式）
     */
    public static void removeLinks(String key) {
        CopyOnWriteArrayList<Session> removed = websocketMap.remove(key);
        if (removed != null) {
            logger.info("用户关闭所有连接：{}，连接数:{}", key, removed.size());
        }
    }

    /**
     * 获取所有客户端
     */
    public static ConcurrentHashMap<String, CopyOnWriteArrayList<Session>> getAllSession() {
        return websocketMap;
    }

    /**
     * 判断客户端是否在线
     */
    public static Boolean containsKey(String key) {
        CopyOnWriteArrayList<Session> sessions = websocketMap.get(key);
        return sessions != null && !sessions.isEmpty();
    }

    /**
     * 获取指定 key 的第一个 Session（兼容旧调用）
     */
    public static Session getSession(String key) {
        CopyOnWriteArrayList<Session> sessions = websocketMap.get(key);
        return (sessions != null && !sessions.isEmpty()) ? sessions.get(0) : null;
    }

    /**
     * 向指定 Session 发送消息
     */
    public static void sendMessage(Session session, Object message) {
        try {
            if (session != null && session.isOpen() && message != null) {
                session.getAsyncRemote().sendText(JsonUtils.toJsonString(message));
            }
        } catch (Exception e) {
            logger.error("WebSocket发送消息失败, sessionId:{}", session != null ? session.getId() : "null", e);
        }
    }

    /**
     * 向指定 key 的所有连接发送消息（Object）
     */
    public static void sendMessage(String key, Object message) {
        sendToAllSessions(key, JsonUtils.toJsonString(message));
    }

    /**
     * 向指定 key 的所有连接发送带类型的消息
     */
    public static <T> void sendMessage(String key, int type, T data) {
        try {
            if (data != null) {
                WebSocketMessage<T> msg = new WebSocketMessage<>(type, data);
                sendToAllSessions(key, JsonUtils.toJsonString(msg));
            }
        } catch (Exception e) {
            logger.error("WebSocket发送消息失败, key:{}, type:{}", key, type, e);
        }
    }

    /**
     * 向指定 key 的所有连接发送消息（String）
     */
    public static void sendMessage(String key, String message) {
        sendToAllSessions(key, message);
    }

    /**
     * 向指定 key 的所有活跃 Session 发送文本消息
     */
    private static void sendToAllSessions(String key, String text) {
        CopyOnWriteArrayList<Session> sessions = websocketMap.get(key);
        if (sessions == null || text == null) return;
        for (Session session : sessions) {
            try {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(text);
                }
            } catch (Exception e) {
                logger.error("WebSocket发送消息失败, key:{}, sessionId:{}", key, session.getId(), e);
            }
        }
    }

    /**
     * 给包括当前客户端的全部客户端发送消息
     */
    public static void sendAll(String message) {
        for (Map.Entry<String, CopyOnWriteArrayList<Session>> entry : websocketMap.entrySet()) {
            for (Session session : entry.getValue()) {
                try {
                    if (session.isOpen()) {
                        session.getAsyncRemote().sendText(message);
                    }
                } catch (Exception e) {
                    logger.error("WebSocket群发失败, key:{}, sessionId:{}", entry.getKey(), session.getId(), e);
                }
            }
        }
    }
}
