package com.aurora.business.service.impl.configure;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.configure.ToSidebarBo;
import com.aurora.business.domain.entity.configure.ToSidebar;
import com.aurora.business.domain.vo.configure.ToSidebarVo;
import com.aurora.business.mapper.configure.ToSidebarMapper;
import com.aurora.business.service.configure.IToSidebarService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 侧边栏配置Service业务层处理
 *
 * @author aurora
 * @date 2022-09-16
 */
@RequiredArgsConstructor
@Service
public class ToSidebarServiceImpl implements IToSidebarService {

    private final ToSidebarMapper baseMapper;

    /**
     * 查询侧边栏配置
     *
     * @param id 侧边栏配置主键
     * @return 侧边栏配置
     */
    @Override
    public ToSidebarVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询侧边栏配置列表
     *
     * @param bo 侧边栏配置
     * @return 侧边栏配置
     */
    @Override
    public TableDataInfo<ToSidebarVo> queryPageList(ToSidebarBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ToSidebar> lqw = buildQueryWrapper(bo);
        Page<ToSidebarVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询侧边栏配置列表
     *
     * @param bo 侧边栏配置
     * @return 侧边栏配置
     */
    @Override
    public List<ToSidebarVo> queryList(ToSidebarBo bo) {
        LambdaQueryWrapper<ToSidebar> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ToSidebar> buildQueryWrapper(ToSidebarBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ToSidebar> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getSidebarName()), ToSidebar::getSidebarName, bo.getSidebarName());
        lqw.eq(StringUtils.isNotBlank(bo.getSidebarIcon()), ToSidebar::getSidebarIcon, bo.getSidebarIcon());
        lqw.eq(bo.getOrder() != null, ToSidebar::getOrder, bo.getOrder());
        lqw.eq(bo.getIsList() != null, ToSidebar::getIsList, bo.getIsList());
        lqw.eq(bo.getStatus() != null, ToSidebar::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRoute()), ToSidebar::getRoute, bo.getRoute());
        return lqw;
    }

    /**
     * 新增侧边栏配置
     *
     * @param bo 侧边栏配置
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ToSidebarBo bo) {
        ToSidebar add = BeanUtil.toBean(bo, ToSidebar.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改侧边栏配置
     *
     * @param bo 侧边栏配置
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ToSidebarBo bo) {
        ToSidebar update = BeanUtil.toBean(bo, ToSidebar.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(ToSidebar entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除侧边栏配置
     *
     * @param ids 需要删除的侧边栏配置主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}

