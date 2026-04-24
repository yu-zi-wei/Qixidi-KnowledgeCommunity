package com.qixidi.business.api.frontDesk.webSocket;

import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketSelector;
import com.light.webSocket.utils.WebSocketUtils;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jodd.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 用户 WebSocket 端点（单连接，统一推送所有消息类型）
 *
 * @author ziwei
 * @date 2024年01月05日
 */
@Component
@ServerEndpoint(value = "/websocket/{userId}")
public class WebSocketUserApi {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketUserApi.class);
    //当前客户端名称
    private String key = "";
    private Session session;

    /**
     * 建立链接，连接后推送所有类型的初始数据
     *
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.key = userId;
        this.session = session;
        //建立链接
        WebSocketUtils.addLinks(userId, session);
        try {
            // 推送站内通知汇总
            WebSocketSelector.execute(WebSocketEnum.INSIDE_NOTICE).execute(userId);
            // 推送私信红点
            WebSocketSelector.execute(WebSocketEnum.PERSONAL_RED_DOT).execute(userId);
        } catch (Exception e) {
            logger.error("WebSocket初始推送失败, userId:{}", userId, e);
        }
    }

    /**
     * 关闭链接
     */
    @OnClose
    public void onClose() {
        WebSocketUtils.removeLinks(key, session);
    }

    /**
     * 服务端接收到消息
     *
     * @param message
     * @param session
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        if (StringUtil.isNotEmpty(message)) {
//            logger.info("收到用户消息:{},报文:{}", key, message);
//        }
    }

    // ===================== 加上下面这个方法 =====================
    @OnError
    public void onError(Session session, Throwable throwable) {
        // EOFException 是客户端断开，属于正常现象，只需要静默处理
        if (throwable instanceof IOException) {
            logger.debug("WebSocket 客户端断开连接：{}", session.getId());
            return;
        }
        // 其他错误才打印
        logger.error("WebSocket 异常", throwable);
    }
}
