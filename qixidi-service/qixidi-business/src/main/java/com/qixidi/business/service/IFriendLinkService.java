package com.qixidi.business.service;

import com.qixidi.business.domain.bo.FriendLinkBo;
import com.qixidi.business.domain.vo.FriendLinkVo;
import com.light.core.core.page.TableDataInfo;

import java.util.List;

/**
 * 友链Service接口（后台管理；前台展示走 SiteInfoService#friendLink）
 *
 * @author ziwei
 */
public interface IFriendLinkService {

    /**
     * 后台友链分页（名称模糊 + 状态多选筛选）
     */
    TableDataInfo<FriendLinkVo> queryPageList(FriendLinkBo bo);

    /**
     * 友链详情
     */
    FriendLinkVo queryById(Long id);

    /**
     * 新增友链
     */
    void insertByBo(FriendLinkBo bo);

    /**
     * 修改友链
     */
    void updateByBo(FriendLinkBo bo);

    /**
     * 批量删除友链（物理删）
     */
    void deleteByIds(List<Long> ids);
}
