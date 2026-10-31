package com.qixidi.auth.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.qixidi.auth.domain.bo.SysBlackListBo;
import com.qixidi.auth.domain.entity.SysBlackList;
import com.qixidi.auth.mapper.SysBlackListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * MP 3.5.17 移除 ServiceImpl，改为项目规范的构造器注入 Mapper 风格
 *
 * @author ziwei
 * @date 2024年09月16日
 */
@RequiredArgsConstructor
@Service
public class SysBlackListService {

    private final SysBlackListMapper baseMapper;

    public List<SysBlackList> list(Wrapper<SysBlackList> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    public boolean saveBatch(Collection<SysBlackList> entityList) {
        return baseMapper.insertBatch(entityList);
    }

    /**
     * 后台分页查询（ip like / uid eq / homePlace like，id 倒序）
     */
    public TableDataInfo<SysBlackList> queryPageList(SysBlackListBo bo, PageQuery pageQuery) {
        Page<SysBlackList> page = baseMapper.selectPage(pageQuery.build(), Wrappers.lambdaQuery(SysBlackList.class)
            .like(StringUtils.isNotBlank(bo.getIp()), SysBlackList::getIp, bo.getIp())
            .eq(StringUtils.isNotBlank(bo.getUid()), SysBlackList::getUid, bo.getUid())
            .like(StringUtils.isNotBlank(bo.getHomePlace()), SysBlackList::getHomePlace, bo.getHomePlace())
            .orderByDesc(SysBlackList::getId));
        return TableDataInfo.build(page);
    }

    /**
     * 手动加黑单条
     */
    public void insertBlack(SysBlackList entity) {
        baseMapper.insert(entity);
    }

    /**
     * 批量移出黑名单（物理删除），返回删除行数
     */
    public int deleteByIds(List<Long> ids) {
        return baseMapper.deleteByIds(ids);
    }
}
