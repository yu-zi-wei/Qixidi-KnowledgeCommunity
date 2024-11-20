package com.aurora.business.service.impl.configure;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.aurora.business.domain.bo.configure.ToNavigationBo;
import com.aurora.business.domain.entity.configure.ToNavigation;
import com.aurora.business.domain.vo.configure.ToNavigationVo;
import com.aurora.business.mapper.configure.ToNavigationMapper;
import com.aurora.business.service.configure.IToNavigationService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.RedisKeyEnums;
import com.aurora.common.utils.StringUtils;
import com.aurora.common.utils.redis.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public ToNavigationVo queryById(Long id) {
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
        return queryPageList(lqw);
    }

    @Override
    public void queryPageList() {
        LambdaQueryWrapper<ToNavigation> lqw = Wrappers.lambdaQuery();
        lqw.orderByAsc(ToNavigation::getOrder);
        TableDataInfo<ToNavigationVo> toNavigationVoTableDataInfo = queryPageList(lqw);
        RedisUtils.setCacheObject(RedisKeyEnums.NAVIGATION_BAR_CONFIGURATION.getKey(), toNavigationVoTableDataInfo.getRows());
    }

    public TableDataInfo<ToNavigationVo> queryPageList(LambdaQueryWrapper<ToNavigation> lqw) {
        List<ToNavigationVo> result = baseMapper.selectVoList(lqw);
        if (CollectionUtil.isEmpty(result)) return new TableDataInfo();
        //父级
        List<ToNavigationVo> parentList = result.stream().filter(item -> item.getParentId() == null).collect(Collectors.toList());
        List<ToNavigationVo> list = result.stream().filter(item -> item.getParentId() != null).collect(Collectors.toList());
        parentList.forEach(item -> {
            if (item.getIsList() == 1) {
                List<ToNavigationVo> list1 = new ArrayList();
                list.forEach(items -> {
                    if (items.getParentId().equals(item.getId())) {
                        list1.add(items);
                    }
                });
                item.setLevelList(list1);
            }
        });
        return TableDataInfo.build(parentList);
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
        LambdaQueryWrapper<ToNavigation> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getNavigationName()), ToNavigation::getNavigationName, bo.getNavigationName());
        lqw.eq(StringUtils.isNotBlank(bo.getNavigationIcon()), ToNavigation::getNavigationIcon, bo.getNavigationIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getRoute()), ToNavigation::getRoute, bo.getRoute());
        lqw.eq(bo.getOrder() != null, ToNavigation::getOrder, bo.getOrder());
        lqw.eq(bo.getIsList() != null, ToNavigation::getIsList, bo.getIsList());
        lqw.eq(bo.getStatus() != null, ToNavigation::getStatus, bo.getStatus());
        lqw.eq(bo.getType() != null, ToNavigation::getType, bo.getType());
        lqw.orderByAsc(ToNavigation::getOrder);
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
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除导航栏配置
     *
     * @param ids 需要删除的导航栏配置主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<ToNavigationVo> queryPageListAdmin(ToNavigationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ToNavigation> lqw = buildQueryWrapper(bo);
        IPage<ToNavigationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }
}

