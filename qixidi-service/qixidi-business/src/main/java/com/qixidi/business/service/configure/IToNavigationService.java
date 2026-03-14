package com.qixidi.business.service.configure;

import com.qixidi.business.domain.bo.configure.ToNavigationBo;
import com.qixidi.business.domain.vo.configure.ToNavigationVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 导航栏配置Service接口
 *
 * @author aurora
 * @date 2022-09-16
 */
public interface IToNavigationService {

    /**
     * 查询导航栏配置
     *
     * @param id 导航栏配置主键
     * @return 导航栏配置
     */
    ToNavigationVo queryById(Long id);

    /**
     * 查询导航栏配置列表
     *
     * @param bo 导航栏配置
     * @return 导航栏配置集合
     */
    TableDataInfo<ToNavigationVo> queryPageList(ToNavigationBo bo, PageQuery pageQuery);

    void queryPageList();

    /**
     * 查询导航栏配置列表
     *
     * @param bo 导航栏配置
     * @return 导航栏配置集合
     */
    List<ToNavigationVo> queryList(ToNavigationBo bo);

    /**
     * 修改导航栏配置
     *
     * @param bo 导航栏配置
     * @return 结果
     */
    Boolean insertByBo(ToNavigationBo bo);

    /**
     * 修改导航栏配置
     *
     * @param bo 导航栏配置
     * @return 结果
     */
    Boolean updateByBo(ToNavigationBo bo);

    /**
     * 校验并批量删除导航栏配置信息
     *
     * @param ids     需要删除的导航栏配置主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    TableDataInfo<ToNavigationVo> queryPageListAdmin(ToNavigationBo bo, PageQuery pageQuery);
}
