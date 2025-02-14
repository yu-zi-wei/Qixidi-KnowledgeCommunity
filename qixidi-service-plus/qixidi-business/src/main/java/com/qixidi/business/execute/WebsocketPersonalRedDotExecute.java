package com.qixidi.business.execute;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.constant.WebSocketConstant;
import com.qixidi.business.domain.enums.WebSocketEnum;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.qixidi.business.selector.webSocket.WebSocketInterface;
import com.qixidi.business.service.privateUser.IPrivateUserService;
import com.qixidi.business.service.webSocket.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zi-wei
 * @create 2024/12/11 15:00
 */
@Service
public class WebsocketPersonalRedDotExecute implements WebSocketInterface {
    @Autowired
    private IPrivateUserService iPrivateUserService;
    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.PERSONAL_RED_DOT;
    }

    @Override
    public void execute(String uuid, WebSocketEnum anEnum) {
        PrivateUserBo bo = new PrivateUserBo();
        bo.setUid(uuid.replace(WebSocketConstant.PERSONAL_RED_DOT, ""));
        TableDataInfo<PrivateUserVo> list = iPrivateUserService.queryPageList(bo, new PageQuery());
        webSocketServer.sendMessageToUser(uuid, list);
    }
}
