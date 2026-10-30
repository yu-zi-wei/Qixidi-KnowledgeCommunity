package com.qixidi.business.domain.vo.privateUser;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
import com.qixidi.business.domain.entity.privateUser.PrivateNewsInfo;


/**
 * 私信记录视图对象 b_private_news_info
 *
 * @author aurora
 * @date 2023-03-23
 */
@Data
@ExcelIgnoreUnannotated
public class PrivateNewsInfoVo extends PrivateNewsInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String userAvatar;


}

