package com.aurora.business.service.configure;

import com.aurora.business.domain.bo.configure.ToSidebarBo;
import com.aurora.business.domain.vo.configure.ToSidebarVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 侧边栏配置Service接口
 *
 * @author aurora
 * @date 2022-09-16
 */
public interface IToSidebarService {

    /**
     * 查询侧边栏配置
     *
     * @param id 侧边栏配置主键
     * @return 侧边栏配置
     */
    ToSidebarVo queryById(Long id);

    /**
     * 查询侧边栏配置列表
     *
     * @param bo 侧边栏配置
     * @return 侧边栏配置集合
     */
    TableDataInfo<ToSidebarVo> queryPageList(ToSidebarBo bo, PageQuery pageQuery);

    /**
     * 查询侧边栏配置列表
     *
     * @param bo 侧边栏配置
     * @return 侧边栏配置集合
     */
    List<ToSidebarVo> queryList(ToSidebarBo bo);

    /**
     * 修改侧边栏配置
     *
     * @param bo 侧边栏配置
     * @return 结果
     */
    Boolean insertByBo(ToSidebarBo bo);

    /**
     * 修改侧边栏配置
     *
     * @param bo 侧边栏配置
     * @return 结果
     */
    Boolean updateByBo(ToSidebarBo bo);

    /**
     * 校验并批量删除侧边栏配置信息
     *
     * @param ids 需要删除的侧边栏配置主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
