package com.qixidi.business.execute;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.webSocket.domain.constant.WebSocketConstant;
import com.light.webSocket.domain.enums.WebSocketEnum;
import com.light.webSocket.selector.WebSocketInterface;
import com.light.webSocket.utils.WebSocketUtils;
import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.qixidi.business.service.privateUser.IPrivateUserService;
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

    @Override
    public boolean support(WebSocketEnum anEnum) {
        return anEnum == WebSocketEnum.PERSONAL_RED_DOT;
    }

    @Override
    public void execute(String uuid, WebSocketEnum anEnum) {
        PrivateUserBo bo = new PrivateUserBo();
        bo.setUid(uuid.replace(WebSocketConstant.PERSONAL_RED_DOT, ""));
        TableDataInfo<PrivateUserVo> list = iPrivateUserService.queryPageList(bo, new PageQuery());
        WebSocketUtils.sendMessage(uuid, list);
    }
}
