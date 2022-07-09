package com.aurora.system.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.bo.TripartiteUserBo;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.vo.TripartiteUserVo;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.DeviceType;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.StringUtils;
import com.aurora.system.mapper.TripartiteUserMapper;
import com.aurora.system.service.ITripartiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author ruoyi
 * @date 2022-06-12
 */
@RequiredArgsConstructor
@Service
public class TripartiteUserServiceImpl implements ITripartiteUserService {

    private final TripartiteUserMapper baseMapper;
    /**
     * 查询【请填写功能名称】
     *
     * @param uuid 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    @Override
    public TripartiteUserVo queryById(String uuid) {
        return baseMapper.selectVoById(uuid);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param bo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public TableDataInfo<TripartiteUserVo> queryPageList(TripartiteUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TripartiteUser> lqw = buildQueryWrapper(bo);
        Page<TripartiteUserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询【请填写功能名称】列表
     *
     * @param bo 【请填写功能名称】
     * @return 【请填写功能名称】
     */
    @Override
    public List<TripartiteUserVo> queryList(TripartiteUserBo bo) {
        LambdaQueryWrapper<TripartiteUser> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<TripartiteUser> buildQueryWrapper(TripartiteUserBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<TripartiteUser> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getUsername()), TripartiteUser::getUsername, bo.getUsername());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), TripartiteUser::getNickname, bo.getNickname());
        lqw.eq(StringUtils.isNotBlank(bo.getAvatar()), TripartiteUser::getAvatar, bo.getAvatar());
        lqw.eq(StringUtils.isNotBlank(bo.getBlog()), TripartiteUser::getBlog, bo.getBlog());
        lqw.eq(StringUtils.isNotBlank(bo.getCompany()), TripartiteUser::getCompany, bo.getCompany());
        lqw.eq(StringUtils.isNotBlank(bo.getLocation()), TripartiteUser::getLocation, bo.getLocation());
        lqw.eq(StringUtils.isNotBlank(bo.getEmail()), TripartiteUser::getEmail, bo.getEmail());
        lqw.eq(StringUtils.isNotBlank(bo.getGender()), TripartiteUser::getGender, bo.getGender());
        lqw.eq(StringUtils.isNotBlank(bo.getSource()), TripartiteUser::getSource, bo.getSource());
        return lqw;
    }

    /**
     * 新增【请填写功能名称】
     *
     * @param bo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public Boolean insertByBo(TripartiteUserBo bo) {
        TripartiteUser add = BeanUtil.toBean(bo, TripartiteUser.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUuid(add.getUuid());
        }
        return flag;
    }

    /**
     * 修改【请填写功能名称】
     *
     * @param bo 【请填写功能名称】
     * @return 结果
     */
    @Override
    public Boolean updateByBo(TripartiteUserBo bo) {
        TripartiteUser update = BeanUtil.toBean(bo, TripartiteUser.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(TripartiteUser entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除【请填写功能名称】
     *
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void oauthLogin(TripartiteUser tripartiteUser) {
        TripartiteUser uuidInfo = baseMapper.selectOne(new QueryWrapper<TripartiteUser>().eq("uuid", tripartiteUser.getUuid()));
        if (uuidInfo == null) {
            baseMapper.insert(tripartiteUser);
        }
        //记录用户信息，登录
        LoginHelper.tripartiteLoginByDevice(tripartiteUser, DeviceType.PC);
    }
}
