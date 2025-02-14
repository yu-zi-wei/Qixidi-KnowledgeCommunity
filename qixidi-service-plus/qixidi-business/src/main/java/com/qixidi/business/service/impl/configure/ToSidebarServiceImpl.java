package com.qixidi.business.service.impl.configure;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.configure.ToNavigationBo;
import com.qixidi.business.domain.bo.configure.ToSidebarBo;
import com.qixidi.business.domain.entity.configure.ToSidebar;
import com.qixidi.business.domain.vo.configure.ToSidebarVo;
import com.qixidi.business.mapper.configure.ToSidebarMapper;
import com.qixidi.business.service.configure.IToSidebarService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.light.core.utils.JsonUtils;
import com.light.core.utils.StringUtils;
import com.light.redission.utils.RedisUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public ToSidebarVo queryById(Long id) {
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
        LambdaQueryWrapper<ToSidebar> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getSidebarName()), ToSidebar::getSidebarName, bo.getSidebarName());
        lqw.eq(StringUtils.isNotBlank(bo.getSidebarIcon()), ToSidebar::getSidebarIcon, bo.getSidebarIcon());
        lqw.eq(bo.getIsList() != null, ToSidebar::getIsList, bo.getIsList());
        lqw.eq(bo.getStatus() != null, ToSidebar::getStatus, bo.getStatus());
        lqw.eq(bo.getType() != null, ToSidebar::getType, bo.getType());
        lqw.eq(StringUtils.isNotBlank(bo.getRoute()), ToSidebar::getRoute, bo.getRoute());
        lqw.orderByAsc(ToSidebar::getOrder);
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
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除侧边栏配置
     *
     * @param ids 需要删除的侧边栏配置主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<ToSidebarVo> sidebarList(ToNavigationBo bo) {
        Object cacheObject = RedisUtils.getCacheObject(RedisBusinessKeyEnums.SIDEBAR_CONFIGURATION.getKey());
        if (ObjectUtils.isNotEmpty(cacheObject)) {
            List<ToSidebarVo> toSidebarVos = JsonUtils.castList(cacheObject, ToSidebarVo.class);
            List<ToSidebarVo> collect = toSidebarVos.stream().filter(item -> item.getType().equals(bo.getType())).collect(Collectors.toList());
            return collect;
        }
//        缓存数据
        sidebarList();
        QueryWrapper<ToSidebar> lqw = new QueryWrapper<ToSidebar>().orderByAsc("`order`").eq("type", bo.getType());
        return sidebarList(lqw);
    }

    @Override
    public void sidebarList() {
        List<ToSidebarVo> toSidebarVos = sidebarList(new QueryWrapper<ToSidebar>().orderByAsc("`order`"));
        RedisUtils.setCacheObject(RedisBusinessKeyEnums.SIDEBAR_CONFIGURATION.getKey(), toSidebarVos);
    }

    public List<ToSidebarVo> sidebarList(QueryWrapper<ToSidebar> lqw) {
        List<ToSidebarVo> toSidebarVos = baseMapper.selectVoList(lqw);
        //父级菜单
        List<ToSidebarVo> parentList = toSidebarVos.stream().filter(item -> item.getParentId() == null).collect(Collectors.toList());
        //二级菜单
        List<ToSidebarVo> list = toSidebarVos.stream().filter(item -> item.getParentId() != null).collect(Collectors.toList());
        parentList.forEach(item -> {
            if (item.getIsList() == 1) {
                List<ToSidebarVo> list1 = new ArrayList();
                list.forEach(items -> {
                    if (items.getParentId().equals(item.getId())) {
                        list1.add(items);
                    }
                });
                item.setLevelList(list1);
            }
        });
        return parentList;
    }

}

