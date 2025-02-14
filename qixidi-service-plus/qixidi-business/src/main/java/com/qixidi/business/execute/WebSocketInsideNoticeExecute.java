package com.qixidi.business.execute;


import com.qixidi.business.domain.enums.WebSocketEnum;
import com.qixidi.business.domain.vo.news.NewsUserSumVo;
import com.qixidi.business.selector.webSocket.WebSocketInterface;
import com.qixidi.business.service.impl.news.NewsUserInfoServiceImpl;
import com.qixidi.business.service.webSocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ziwei
 * @date 2024年01月07日
 */
@Service
public class WebSocketInsideNoticeExecute implements WebSocketInterface {
    @Autowired
    private WebSocketServer webSocketServer;
    @Autowired
    private NewsUserInfoServiceImpl newsUserInfoService;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.INSIDE_NOTICE;
    }

    @Override
    public void execute(String uuid, WebSocketEnum anEnum) {
        List<NewsUserSumVo> list = newsUserInfoService.pushOne(uuid);
        webSocketServer.sendMessageToUser(uuid, list);
    }
}
