package com.aurora.system.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.ReUtil;
import com.aurora.business.domain.bo.user.TripartiteUserBo;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.model.LoginUserMain;
import com.aurora.common.core.domain.model.RegisterUserMain;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.enums.DeviceType;
import com.aurora.common.enums.UserStatus;
import com.aurora.common.exception.ServiceException;
import com.aurora.common.helper.LoginHelper;
import com.aurora.common.utils.RandomNumberUtils;
import com.aurora.common.utils.StringUtils;
import com.aurora.system.mapper.TripartiteUserMapper;
import com.aurora.system.service.ITripartiteUserService;
import com.aurora.utils.SecureUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
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
            tripartiteUser.setCreateTime(new Date());
            baseMapper.insert(tripartiteUser);
        }
        //记录用户信息，登录
        LoginHelper.tripartiteLoginByDevice(uuidInfo, DeviceType.PC);
    }

    @Override
    public Map isLogin() {
        if (!StpUtil.isLogin()) return null;
        Map map = Maps.newHashMap();
        TripartiteUser tripartiteUsers = LoginHelper.getTripartiteUser();
        String loginIdAsString = StpUtil.getLoginIdAsString();
        LoginHelper.getLoginUser();
        if (tripartiteUsers == null) return map;
        Boolean isBackstage = tripartiteUsers.getUserType().equals("tripartite_user") ? false : true;
        map.put("uuid", loginIdAsString);
        map.put("tokenInfo", StpUtil.getTokenInfo());
        map.put("isLogin", StpUtil.isLogin());
        map.put("isBackstage", isBackstage);
        String substring = loginIdAsString.substring(0, loginIdAsString.indexOf(":"));
        String uuid = loginIdAsString.substring(substring.length() + 1);
        TripartiteUser tripartiteUser = baseMapper.selectOne(new QueryWrapper<TripartiteUser>().eq("uuid", uuid));
        map.put("tripartiteUser", tripartiteUser);
        return map;
    }

    @Override
    public void frontDeskLogin(LoginUserMain loginUserMain) {
        loginUserMain.setUsername(Base64.decodeStr(loginUserMain.getUsername()));
        loginUserMain.setPassword(Base64.decodeStr(loginUserMain.getPassword()));
        TripartiteUser tripartiteUser = new TripartiteUser();
        BeanUtils.copyProperties(loginUserMain, tripartiteUser);

        TripartiteUser tripartiteUsers = baseMapper.selectOne(new QueryWrapper<TripartiteUser>()
            .eq("email", tripartiteUser.getUsername()).or()
            .eq("phone", tripartiteUser.getUsername()));
        if (ObjectUtils.isEmpty(tripartiteUsers)) throw new ServiceException("用户名错误！");
        if (!SecureUtils.digesters(loginUserMain.getPassword()).equals(tripartiteUsers.getPassword()))
            throw new ServiceException("密码错误！");

        //记录用户信息，登录
        LoginHelper.tripartiteLoginByDevice(tripartiteUsers, DeviceType.PC);
    }

    @Override
    public R register(RegisterUserMain registerUserMain) {
        registerUserMain.setPassword(Base64.decodeStr(registerUserMain.getPassword()));
        registerUserMain.setEmail(Base64.decodeStr(registerUserMain.getEmail()));
        boolean isEmail = ReUtil.isMatch("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", registerUserMain.getEmail());
        if (!isEmail) throw new ServiceException("邮箱格式错误！");
        TripartiteUser tripartiteUser = new TripartiteUser();
        BeanUtils.copyProperties(registerUserMain, tripartiteUser);
        TripartiteUser tripartiteUser1 = baseMapper.selectOne(new QueryWrapper<TripartiteUser>()
            .eq("email", tripartiteUser.getEmail()));
        if (registerUserMain.getRegisterType() == 1 && ObjectUtils.isNotEmpty(tripartiteUser1))
            throw new ServiceException("该邮箱已被使用！");
        tripartiteUser.setUuid(RandomNumberUtils.Snowflakes().toString())
            .setUsername("道友" + RandomNumberUtils.Snowflakes())
            .setNickname("道友" + RandomNumberUtils.Snowflakes())
            .setCreateTime(new Date())
            .setPassword(SecureUtils.digesters(tripartiteUser.getPassword()))
            .setState(UserStatus.OK.getIntegerCode())
            .setRoleId(UserStatus.GENERAL_USER.getLogCode())
            .setSource("平台注册");
        int insert = baseMapper.insert(tripartiteUser);
        return insert > 0 ? R.ok("注册成功！") : R.fail("注册异常！");
    }

}
