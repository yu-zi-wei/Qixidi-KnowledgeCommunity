package com.aurora.business.service.impl.configure;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.configure.ToNavigationBo;
import com.aurora.business.domain.entity.configure.ToNavigation;
import com.aurora.business.domain.vo.configure.ToNavigationVo;
import com.aurora.business.mapper.configure.ToNavigationMapper;
import com.aurora.business.service.configure.IToNavigationService;
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
 * 导航栏配置Service业务层处理
 *
 * @author aurora
 * @date 2022-09-16
 */
@RequiredArgsConstructor
@Service
public class ToNavigationServiceImpl implements IToNavigationService {

    private final ToNavigationMapper baseMapper;

    /**
     * 查询导航栏配置
     *
     * @param id 导航栏配置主键
     * @return 导航栏配置
     */
    @Override
    public ToNavigationVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询导航栏配置列表
     *
     * @param bo 导航栏配置
     * @return 导航栏配置
     */
    @Override
    public TableDataInfo<ToNavigationVo> queryPageList(ToNavigationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ToNavigation> lqw = buildQueryWrapper(bo);
        Page<ToNavigationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询导航栏配置列表
     *
     * @param bo 导航栏配置
     * @return 导航栏配置
     */
    @Override
    public List<ToNavigationVo> queryList(ToNavigationBo bo) {
        LambdaQueryWrapper<ToNavigation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ToNavigation> buildQueryWrapper(ToNavigationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ToNavigation> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getNavigationName()), ToNavigation::getNavigationName, bo.getNavigationName());
        lqw.eq(StringUtils.isNotBlank(bo.getNavigationIcon()), ToNavigation::getNavigationIcon, bo.getNavigationIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getRoute()), ToNavigation::getRoute, bo.getRoute());
        lqw.eq(bo.getOrder() != null, ToNavigation::getOrder, bo.getOrder());
        lqw.eq(bo.getIsList() != null, ToNavigation::getIsList, bo.getIsList());
        lqw.eq(bo.getStatus() != null, ToNavigation::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增导航栏配置
     *
     * @param bo 导航栏配置
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ToNavigationBo bo) {
        ToNavigation add = BeanUtil.toBean(bo, ToNavigation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改导航栏配置
     *
     * @param bo 导航栏配置
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ToNavigationBo bo) {
        ToNavigation update = BeanUtil.toBean(bo, ToNavigation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(ToNavigation entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除导航栏配置
     *
     * @param ids 需要删除的导航栏配置主键
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

