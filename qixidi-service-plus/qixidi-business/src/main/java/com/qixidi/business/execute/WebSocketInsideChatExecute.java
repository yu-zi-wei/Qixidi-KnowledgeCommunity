package com.qixidi.business.execute;

import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketInterface;
import com.qixidi.business.service.impl.news.NewsUserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 站内聊天
 *
 * @author ziwei
 * @date 2024年01月28日
 */
@Service
public class WebSocketInsideChatExecute implements WebSocketInterface {
    @Autowired
    private NewsUserInfoServiceImpl newsUserInfoService;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.INSIDE_CHAT;
    }

    @Override
    public void execute(String uuid, WebSocketEnum anEnum) {

    }

}
