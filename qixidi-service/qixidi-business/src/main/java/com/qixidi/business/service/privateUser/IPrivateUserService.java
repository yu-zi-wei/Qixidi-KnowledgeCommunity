package com.qixidi.business.service.privateUser;

import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

/**
 * 私信记录Service接口
 *
 * @author aurora
 * @date 2023-03-23
 */
public interface IPrivateUserService {

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录集合
     */
    TableDataInfo<PrivateUserVo> queryPageList(PrivateUserBo bo, PageQuery pageQuery);

    Boolean deleteById(String id);

    Boolean deleteAll();

    boolean addPrivateUser(String targetUid);
}
