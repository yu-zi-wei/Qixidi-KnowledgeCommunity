package com.qixidi.love.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.LoverMail;
import com.qixidi.love.domain.LoverConfig;
import com.qixidi.love.domain.bo.LoverConfigBo;
import com.qixidi.love.domain.vo.LoverConfigVo;
import com.qixidi.love.mapper.LoverCongfigMapper;
import com.qixidi.love.service.ILoverConfigService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基本配置Service业务层处理
 *
 * @author ziwei
 * @date 2022-12-02
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class LoverConfigServiceImpl implements ILoverConfigService {

    private final LoverCongfigMapper baseMapper;

    /**
     * 查询基本配置
     */
    @Override
    public LoverConfigVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询基本配置列表
     */
    @Override
    public TableDataInfo<LoverConfigVo> queryPageList(LoverConfigBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LoverConfig> lqw = buildQueryWrapper(bo);
        Page<LoverConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询基本配置列表
     */
    @Override
    public List<LoverConfigVo> queryList(LoverConfigBo bo) {
        LambdaQueryWrapper<LoverConfig> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LoverConfig> buildQueryWrapper(LoverConfigBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LoverConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getMail()), LoverConfig::getMail, bo.getMail());
        lqw.eq(StringUtils.isNotBlank(bo.getFilings()), LoverConfig::getFilings, bo.getFilings());
        lqw.like(StringUtils.isNotBlank(bo.getRealmName()), LoverConfig::getRealmName, bo.getRealmName());
        lqw.orderByAsc(LoverConfig::getId);
        return lqw;
    }

    /**
     * 新增基本配置
     */
    @Override
    public Boolean insertByBo(LoverConfigBo bo) {
        LoverConfig add = BeanUtil.toBean(bo, LoverConfig.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改基本配置
     */
    @Override
    public Boolean updateByBo(LoverConfigBo bo) {
        LoverConfig update = BeanUtil.toBean(bo, LoverConfig.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除基本配置
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 初始化邮箱配置
     */
    @Override
    public void initializationMail() {
        String mail = baseMapper.selectMail();
        LoverMail.MailData.setMail(mail);
    }

    @Override
    public LoverConfigVo configInfo() {
        return baseMapper.configInfo();
    }
}

