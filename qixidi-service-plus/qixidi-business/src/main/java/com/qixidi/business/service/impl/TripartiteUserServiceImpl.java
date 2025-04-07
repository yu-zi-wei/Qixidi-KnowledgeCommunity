package com.qixidi.business.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.light.core.config.SmsSendingConfig;
import com.light.core.config.justAuth.BaiDuPlatformConfig;
import com.light.core.config.justAuth.GiteePlatformConfig;
import com.light.core.config.justAuth.WeiBoPlatformConfig;
import com.light.core.config.justAuth.ZhiFuBaoPlatformConfig;
import com.light.core.constant.SystemConstant;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.light.core.enums.DeviceType;
import com.light.core.enums.MsgEnums;
import com.light.core.utils.DateUtils;
import com.light.core.utils.RandomNumberUtils;
import com.light.core.utils.SecureUtils;
import com.light.core.utils.StringUtils;
import com.light.core.utils.email.MailUtils;
import com.light.core.utils.ip.AddressUtils;
import com.light.exception.ServiceException;
import com.light.mybatisPlus.domain.dto.UserInfoIdNameDto;
import com.light.mybatisPlus.interfaces.UserInfoInterface;
import com.light.redission.utils.RedisUtils;
import com.light.webSocket.utils.WebSocketUtils;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.enums.UserStatus;
import com.qixidi.auth.domain.model.LoginUser;
import com.qixidi.auth.domain.model.LoginUserMain;
import com.qixidi.auth.domain.model.PhoneBinding;
import com.qixidi.auth.domain.model.RegisterUserMain;
import com.qixidi.auth.helper.LoginHelper;
import com.qixidi.business.domain.bo.user.TripartiteUserBo;
import com.qixidi.business.domain.bo.user.UserBindBo;
import com.qixidi.business.domain.bo.user.UserInfoBo;
import com.qixidi.business.domain.entity.count.CountUserWebsiteEntity;
import com.qixidi.business.domain.entity.user.UserFollow;
import com.qixidi.business.domain.entity.user.UserInformation;
import com.qixidi.business.domain.enums.OutCodeType;
import com.qixidi.business.domain.enums.RedisBusinessKeyEnums;
import com.qixidi.business.domain.enums.UserFollowType;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.domain.vo.user.UserFollowVo;
import com.qixidi.business.domain.vo.user.UserSimpleInfoVo;
import com.qixidi.business.mapper.TripartiteUserMapper;
import com.qixidi.business.mapper.count.CountUserWebsiteMapper;
import com.qixidi.business.mapper.user.UserFollowMapper;
import com.qixidi.business.mapper.user.UserInformationMapper;
import com.qixidi.business.service.ITripartiteUserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.request.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 平台用户Service业务层处理
 *
 * @author ziwei
 * @date 2022-06-12
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class TripartiteUserServiceImpl implements ITripartiteUserService, UserInfoInterface {

    private final TripartiteUserMapper baseMapper;
    private final CountUserWebsiteMapper countUserWebsiteMapper;
    private final UserInformationMapper userInformationMapper;
    private final UserFollowMapper userFollowMapper;
    @Resource(name = "threadPoolInstance")
    private ExecutorService executorService;
    @Autowired
    private SmsSendingConfig smsSendingConfig;
    @Autowired
    private GiteePlatformConfig giteePlatformConfig;
    @Autowired
    private BaiDuPlatformConfig baiDuPlatformConfig;
    @Autowired
    private WeiBoPlatformConfig weiBoPlatformConfig;
    @Autowired
    private ZhiFuBaoPlatformConfig zhiFuBaoPlatformConfig;

    /**
     * 查询平台用户
     *
     * @param uuid 平台用户主键
     * @return 平台用户
     */
    @Override
    public TripartiteUserVo queryById(String uuid) {
        CountUserWebsiteVo countUserWebsiteVo = countUserWebsiteMapper.selectCensus(uuid);
        TripartiteUserVo tripartiteUserVo = baseMapper.selectVoById(uuid);
        BeanUtils.copyProperties(countUserWebsiteVo, tripartiteUserVo);
        return tripartiteUserVo;
    }

    /**
     * 查询平台用户列表
     *
     * @param bo 平台用户
     * @return 平台用户
     */
    @Override
    public TableDataInfo<TripartiteUserVo> queryPageList(TripartiteUserBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TripartiteUser> lqw = buildQueryWrapper(bo);
        Page<TripartiteUserVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询平台用户列表
     *
     * @param bo 平台用户
     * @return 平台用户
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
        lqw.orderByDesc(TripartiteUser::getCreateTime);
        return lqw;
    }

    /**
     * 新增平台用户
     *
     * @param bo 平台用户
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
     * 修改平台用户
     *
     * @param bo 平台用户
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
     * 批量删除平台用户
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
    @Transactional(rollbackFor = Exception.class)
    public void oauthLogin(TripartiteUser tripartiteUser) {
        TripartiteUser uuidInfo = baseMapper.selectOne(new QueryWrapper<TripartiteUser>().eq("uuid", tripartiteUser.getUuid()));
        if (uuidInfo == null) {
            tripartiteUser.setCreateTime(new Date());
            baseMapper.insert(tripartiteUser);
            CountUserWebsiteEntity countUserWebsiteEntity = new CountUserWebsiteEntity();
            countUserWebsiteEntity.setUuid(tripartiteUser.getUuid());
            countUserWebsiteMapper.insert(countUserWebsiteEntity);
            UserInformation userInformation = new UserInformation()
                    .setUuid(tripartiteUser.getUuid())
                    .setNickname(tripartiteUser.getUsername())
                    .setCreateTime(new Date())
                    .setUpdateId(tripartiteUser.getUuid());
            int insert = userInformationMapper.insert(userInformation);
            if (insert > 0) {
                //            发送邮件
                executorService.execute(() -> {
                    StringBuffer mags = new StringBuffer();
                    mags.append(String.format("【栖息地】新用户注册；用户来源：%s，用户名：%s，手机号：%s，密码：%s",
                            tripartiteUser.getSource(),
                            tripartiteUser.getNickname(), tripartiteUser.getPhone(), tripartiteUser.getPassword()));
                    MailUtils.sendHtml(SystemConstant.AdministratorMailboxList, "【栖息地】新用户注册", mags.toString());
                });
            }
        }
//        更新用户数据
        baseMapper.update(null, new UpdateWrapper<TripartiteUser>()
                .set("username", tripartiteUser.getUsername())
                .set("avatar", tripartiteUser.getAvatar())
                .eq("uuid", tripartiteUser.getUuid()));
        //记录用户信息，登录
        LoginHelper.tripartiteLoginByDevice(tripartiteUser, DeviceType.PC);
    }

    @Override
    public Map isLogin() {
        Map map = Maps.newHashMap();
        TripartiteUser tripartiteUsers = LoginHelper.getTripartiteUser();
        if (tripartiteUsers == null) return map;
        Boolean isBackstage = tripartiteUsers.getUserType().equals("tripartite_user") ? false : true;
        map.put("uuid", tripartiteUsers.getUuid());
        map.put("tokenInfo", StpUtil.getTokenInfo());
        map.put("isLogin", StpUtil.isLogin());
        map.put("isBackstage", isBackstage);
        map.put("tripartiteUser", tripartiteUsers);
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
        if (ObjectUtils.isEmpty(tripartiteUsers)) throw new ServiceException("账号不存在！");
        if (tripartiteUsers.getState().equals(2)) throw new ServiceException("账号已注销！");
        if (tripartiteUsers.getState().equals(1)) throw new ServiceException("账号已冻结！");

        if (!SecureUtils.digesters(loginUserMain.getPassword()).equals(tripartiteUsers.getPassword()))
            throw new ServiceException("密码错误！");

        //记录用户信息，登录
        LoginHelper.tripartiteLoginByDevice(tripartiteUsers, DeviceType.PC);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R register(RegisterUserMain registerUserMain) {
        registerUserMain
                .setPassword(Base64.decodeStr(registerUserMain.getPassword()))
                .setEmail(Base64.decodeStr(registerUserMain.getEmail()))
                .setPhone(Base64.decodeStr(registerUserMain.getPhone()))
                .setCode(Base64.decodeStr(registerUserMain.getCode()));
//        数据校验
        registerDataCheck(registerUserMain);
        TripartiteUser tripartiteUser = new TripartiteUser();
        BeanUtils.copyProperties(registerUserMain, tripartiteUser);
        Long uuid = RandomNumberUtils.Snowflakes();
        tripartiteUser.setUuid(uuid.toString())
                .setUsername("道友" + uuid)
                .setNickname("道友" + uuid)
                .setCreateTime(new Date())
                .setPassword(SecureUtils.digesters(tripartiteUser.getPassword()))
                .setState(UserStatus.OK.getIntegerCode())
                .setRoleId(UserStatus.GENERAL_USER.getLogCode())
                .setSource("平台注册");
        int insert = baseMapper.insert(tripartiteUser);
        CountUserWebsiteEntity countUserWebsiteEntity = new CountUserWebsiteEntity()
                .setUuid(tripartiteUser.getUuid());
        countUserWebsiteMapper.insert(countUserWebsiteEntity);
        UserInformation userInformation = new UserInformation()
                .setUuid(tripartiteUser.getUuid())
                .setNickname(tripartiteUser.getNickname())
                .setCreateTime(new Date())
                .setUpdateId(tripartiteUser.getUuid());
        userInformationMapper.insert(userInformation);
        if (insert > 0) {
//            发送邮件
            executorService.execute(() -> {
                StringBuffer mags = new StringBuffer();
                mags.append(String.format("【栖息地】新用户注册；用户来源：%s，用户名：%s，邮箱：%s，密码：%s",
                        "平台注册", tripartiteUser.getNickname(), tripartiteUser.getEmail(), tripartiteUser.getPassword()));
                MailUtils.sendHtml(SystemConstant.AdministratorMailboxList, "【栖息地】新用户注册", mags.toString());
            });
        }
        return insert > 0 ? R.ok("注册成功！") : R.fail("注册异常！");
    }

    private boolean registerDataCheck(RegisterUserMain registerUserMain) {
//校验手机号/邮箱格式
        phoneMatches(registerUserMain.getEmail(), 2);
//        验证码校验
        String redisKey = String.format(RedisBusinessKeyEnums.MAIL_CAPTCHA.getKey(), registerUserMain.getEmail());
        codeVerification(redisKey, registerUserMain.getCode());
//        手机号/邮箱校验
        Long phone = baseMapper.selectCount(new LambdaQueryWrapper<TripartiteUser>()
                .eq(TripartiteUser::getEmail, registerUserMain.getEmail())
                .eq(TripartiteUser::getState, 0));
        if (registerUserMain.getRegisterType().equals(1) && phone > 0)
            throw new ServiceException("该邮箱已被使用！");
        if (registerUserMain.getRegisterType().equals(2) && phone < 0)
            throw new ServiceException("该邮箱未注册！");
        //清除redis验证码
        RedisUtils.deleteObject(redisKey);
        return true;
    }

    @Override
    public TripartiteUserVo websiteInfo() {
        String tripartiteUuid = LoginHelper.getTripartiteUuid();
        if (tripartiteUuid == null) return null;
        return baseMapper.selectWebInfo(tripartiteUuid);
    }

    @Override
    public TripartiteUserVo getWebsiteInfo(String uuid) {
        TripartiteUserVo tripartiteUserVo = baseMapper.selectWebInfo(uuid);
        String uuid1 = LoginHelper.getTripartiteUuid();
        UserFollow userFollow = userFollowMapper.selectOne(new QueryWrapper<UserFollow>()
                .eq("target_id", uuid)
                .eq("type", UserFollowType.b_user_follow.getCode())
                .eq("uid", uuid1));
        if (ObjectUtils.isNotEmpty(userFollow) && userFollow.getTargetId().equals(uuid)) {
            tripartiteUserVo.setIsFollow(true);
        }
        return tripartiteUserVo;
    }

    @Override
    public boolean updateUserAvatar(String uuid, String avatar) {
        return baseMapper.update(null,
                new LambdaUpdateWrapper<TripartiteUser>()
                        .set(TripartiteUser::getAvatar, avatar)
                        .eq(TripartiteUser::getUuid, uuid)) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserInfo(UserInfoBo bo) {
        bo.setUpdateTime(new Date());
        TripartiteUser tripartiteUser = new TripartiteUser();
        UserInformation userInformation = new UserInformation();
        BeanUtils.copyProperties(bo, tripartiteUser);
        BeanUtils.copyProperties(bo, userInformation);
        int uuidUpdate = baseMapper.update(tripartiteUser, new QueryWrapper<TripartiteUser>().eq("uuid", bo.getUuid()));
        int uuidUpdate1 = userInformationMapper.update(userInformation, new QueryWrapper<UserInformation>().eq("uuid", bo.getUuid()));
        return uuidUpdate + uuidUpdate1 > 0;
    }

    @Override
    public TripartiteUserVo BasicsUser() {
        String uuid = LoginHelper.getTripartiteUuid();
        if (uuid == null) return null;
        return baseMapper.getBasicsUser(uuid);
    }

    @Override
    public R sendOutCode(String email, Integer type) {
        String mag = OutCodeType.acquireTypeMessage(type);
        phoneMatches(email, 2);
        TripartiteUser tripartiteUser = baseMapper.selectEmail(email);
        if (type.equals(OutCodeType.RESET_PASSWORD.getCode())) {
            if (tripartiteUser == null) throw new ServiceException("该邮箱未注册，请前往个人信息进行绑定");
        }
        if (type.equals(OutCodeType.MAILBOX_BINDING.getCode()) || type.equals(OutCodeType.SIGN_IN.getCode())) {
            if (tripartiteUser != null) throw new ServiceException("该邮箱已被使用！");
        }
        String mailCaptchaKey = String.format(RedisBusinessKeyEnums.MAIL_CAPTCHA.getKey(), email);
        if (RedisUtils.hasKey(mailCaptchaKey)) throw new ServiceException(MsgEnums.CAPTCHA_ALREADY_EXISTS.getValue());
        String code = RandomUtil.randomNumbers(5);
        StringBuffer mags = new StringBuffer();
        mags.append("<div style=\"text-align: center\">");
        mags.append("<p>验证码</p>");
        mags.append("<p style=\"font-weight: bold;font-size: 26px\">" + code + "</p>");
        mags.append("<p>【栖息地】您正在进行" + mag + "，您的验证码为：<b>" + code + "</b>，转发他人可能导致账号被盗，请勿泄露，谨防被骗。</p>");
        mags.append("</div>");
        log.info("操作类型：{}，邮箱：{}，验证码：{}", mag, email, code);
        try {
            MailUtils.sendHtml(email, "验证码通知", mags.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
//        存入redis有效期60秒
        RedisUtils.setCacheObject(mailCaptchaKey, code, 60, TimeUnit.SECONDS);
        return R.ok();
    }

    @Override
    public List<TripartiteUserVo> fdUserList(UserInfoBo bo) {
        bo.setUuid(LoginHelper.getTripartiteUuid());
        List<TripartiteUserVo> list = new ArrayList<>();
        if (ObjectUtils.isEmpty(bo.getNickname())) return list;

        list = baseMapper.fdUserList(bo);
        if (ObjectUtils.isEmpty(bo.getUuid()) || CollectionUtils.isEmpty(list)) return list;

        List<UserFollowVo> userFollowVos = userFollowMapper.selectVoList(new QueryWrapper<UserFollow>()
                .eq("uid", bo.getUuid()).eq("type", bo.getType()));

        if (CollectionUtils.isEmpty(userFollowVos)) return list;

        Map<String, String> collect = userFollowVos.stream().collect(Collectors.toMap(UserFollowVo::getTargetId, UserFollowVo::getUid));
        list.forEach(item -> {
            if (collect.get(item.getUuid().toString()) != null) {
                item.setIsFollow(true);
            }
        });
        return list;
    }

    @Override
    public CountUserWebsiteEntity fdUserData(String uuid) {
        CountUserWebsiteEntity countUserWebsiteEntity = countUserWebsiteMapper.selectOne(
                new QueryWrapper<CountUserWebsiteEntity>().eq("uuid", uuid));
        return countUserWebsiteEntity;
    }

    @Override
    public R resetPassword(RegisterUserMain registerUserMain) {
        registerUserMain
                .setPassword(Base64.decodeStr(registerUserMain.getPassword()))
                .setEmail(Base64.decodeStr(registerUserMain.getEmail()))
                .setPhone(Base64.decodeStr(registerUserMain.getPhone()))
                .setCode(Base64.decodeStr(registerUserMain.getCode()));
        registerDataCheck(registerUserMain);
        int update = baseMapper.update(null, new LambdaUpdateWrapper<TripartiteUser>()
                .set(TripartiteUser::getPassword, SecureUtils.digesters(registerUserMain.getPassword()))
                .eq(TripartiteUser::getEmail, registerUserMain.getEmail()));
        if (update > 0) {
            TripartiteUser tripartiteUser = baseMapper.selectEmail(registerUserMain.getEmail());
            executorService.execute(() -> {
//            发送邮件
                StringBuffer mags = new StringBuffer();
                mags.append("<div style=\"text-align: center\">");
                mags.append("<p>密码重置</p>");
                mags.append("<p style=\"font-weight: bold;font-size: 26px\">" + tripartiteUser.getNickname() + "</p>");
                mags.append("<p>【栖息地】您的密码已重置成功，请牢记密码，若不是本人操作可前往官网反馈！</p>");
                mags.append("</div>");
                try {
                    MailUtils.sendHtml(tripartiteUser.getEmail(), "密码重置", mags.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return update > 0 ? R.ok() : R.fail();
    }

    @Override
    public UserSimpleInfoVo isOnline(String userid) {
        TripartiteUser tripartiteUser = baseMapper.selectOne(new QueryWrapper<TripartiteUser>().eq("uuid", userid));
        UserSimpleInfoVo userSimpleInfoVo = new UserSimpleInfoVo();
        if (ObjectUtils.isNotEmpty(tripartiteUser)) {
            BeanUtils.copyProperties(tripartiteUser, userSimpleInfoVo);
        }
        userSimpleInfoVo.setIsOnline(WebSocketUtils.containsKey(userid));
        return userSimpleInfoVo;
    }

    @Override
    public boolean accountCancellation(String uuid) {
        return baseMapper.update(null, new UpdateWrapper<TripartiteUser>()
                .set("state", 2)
                .set("update_time", new Date())
                .eq("uuid", uuid)) > 0;
    }

    @Override
    public R sendPhoneCode(String phone, Integer type, HttpServletRequest request) throws Exception {
        TripartiteUser tripartiteUser = baseMapper.selectPhone(phone);
        if (type.equals(OutCodeType.RESET_PASSWORD.getCode())) {
            if (tripartiteUser == null) throw new ServiceException("该手机号未注册，请前往个人信息进行绑定");
        }
        if (type.equals(OutCodeType.PHONE_BINDING.getCode()) || type.equals(OutCodeType.SIGN_IN.getCode())) {
            if (tripartiteUser != null) throw new ServiceException("该手机号已被使用！");
        }
        String CAPTCHA_DAILY_LIMIT_PHONE = String.format(RedisBusinessKeyEnums.CAPTCHA_DAILY_LIMIT_PHONE.getKey(), phone);
        String ip = AddressUtils.gainIp(request);
        String CAPTCHA_DAILY_LIMIT_IP = String.format(RedisBusinessKeyEnums.CAPTCHA_DAILY_LIMIT_IP.getKey(), ip);
        Long phoneLimit = RedisUtils.getCacheObject(CAPTCHA_DAILY_LIMIT_PHONE);
        Long ipLimit = RedisUtils.getCacheObject(CAPTCHA_DAILY_LIMIT_IP);
        if (phoneLimit == null) phoneLimit = 0L;
        if (ipLimit == null) ipLimit = 0L;
        if (phoneLimit >= 10 || ipLimit >= 10) {
            throw new ServiceException(MsgEnums.VERIFICATION_CODE_UNDERCOUNT.getValue());
        }
        String mailCaptchaKey = String.format(RedisBusinessKeyEnums.PHONE_CAPTCHA.getKey(), phone);
        if (RedisUtils.hasKey(mailCaptchaKey)) throw new ServiceException(MsgEnums.CAPTCHA_ALREADY_EXISTS.getValue());
        String code = RandomUtil.randomNumbers(5);
        Boolean aBoolean = smsSendingConfig.SendSmsCode(phone, code);
        if (aBoolean) {
            long remainingTime = DateUtils.getRemainingTime();//今日剩余秒
            RedisUtils.setCacheObject(CAPTCHA_DAILY_LIMIT_PHONE, phoneLimit + 1, remainingTime, TimeUnit.SECONDS);
            RedisUtils.setCacheObject(CAPTCHA_DAILY_LIMIT_IP, ipLimit + 1, remainingTime, TimeUnit.SECONDS);
            //        存入redis有效期60秒
            RedisUtils.setCacheObject(mailCaptchaKey, code, 180, TimeUnit.SECONDS);
        }
        return R.ok();
    }

    public void phoneMatches(String data, Integer type) {
        if (type.equals(1)) {
            if (!Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", data))
                //手机号码格式错误
                throw new ServiceException("手机号格式错误");
        }
        if (type.equals(2)) {
            if (!ReUtil.isMatch("\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}", data))
                throw new ServiceException("邮箱格式错误！");
        }
    }

    /**
     * 验证码校验
     * * @param key
     *
     * @param code
     */
    public void codeVerification(String key, String code) {
        String cacheSet = RedisUtils.getCacheObject(key);
        if (ObjectUtils.isEmpty(cacheSet))
            throw new ServiceException(MsgEnums.VERIFICATION_CODE_EXPIRED.getValue());
        if (!code.equals(cacheSet))
            throw new ServiceException(MsgEnums.VERIFICATION_CODE_ERROR.getValue());
    }

    @Override
    public Boolean phoneNumberBinding(PhoneBinding phoneBinding) {
//        手机号校验
        phoneMatches(phoneBinding.getPhone(), 1);
//        验证码校验
        String redisKey = String.format(RedisBusinessKeyEnums.PHONE_CAPTCHA.getKey(), phoneBinding.getPhone());
        codeVerification(redisKey, phoneBinding.getCode());
        //清除redis验证码
        RedisUtils.deleteObject(redisKey);
        String uuid = LoginHelper.getTripartiteUuid();
        int update = baseMapper.update(null, new UpdateWrapper<TripartiteUser>()
                .set("phone", phoneBinding.getPhone())
                .set("update_time", new Date())
                .eq("uuid", uuid)
        );
        return update > 0;
    }

    @Override
    public Boolean bindEmail(UserBindBo bo) {
        List<TripartiteUserVo> tripartiteUserVo = baseMapper.selectPrivacy();
        if (bo.getType().equals(1) || bo.getType().equals(2)) {//  邮箱绑定
            phoneMatches(bo.getEmail(), 2);
            Map<String, String> collect = tripartiteUserVo.stream().collect(Collectors.toMap(TripartiteUserVo::getEmail, TripartiteUserVo::getUuid));
            if (collect.get(bo.getEmail()) != null && !collect.get(bo.getEmail()).equals(bo.getUuid()))
                throw new ServiceException("该邮箱已被使用");
//            验证码校验
            String redisKey = String.format(RedisBusinessKeyEnums.MAIL_CAPTCHA.getKey(), bo.getEmail());
            if (bo.getType().equals(2)) {//换绑
                redisKey = String.format(RedisBusinessKeyEnums.MAIL_CAPTCHA.getKey(), bo.getOriginalData());
            }
            codeVerification(redisKey, bo.getCode());
            RedisUtils.deleteObject(redisKey);
            int update = baseMapper.update(null, new UpdateWrapper<TripartiteUser>()
                    .set("email", bo.getEmail()).eq("uuid", bo.getUuid()));
            return update > 0;
        }
        if (bo.getType().equals(3)) {// 手机号换绑
            phoneMatches(bo.getPhone(), 1);
            Map<String, String> collect = tripartiteUserVo.stream().collect(Collectors.toMap(TripartiteUserVo::getPhone, TripartiteUserVo::getPhone));
            if (collect.get(bo.getPhone()) != null && !collect.get(bo.getPhone()).equals(bo.getUuid()))
                throw new ServiceException("手机号已被使用");

            String redisKey = String.format(RedisBusinessKeyEnums.PHONE_CAPTCHA.getKey(), bo.getOriginalData());
            codeVerification(redisKey, bo.getCode());
            RedisUtils.deleteObject(redisKey);
            int update = baseMapper.update(null, new UpdateWrapper<TripartiteUser>()
                    .set("phone", bo.getPhone()).eq("uuid", bo.getUuid()));
            return update > 0;
        }
        if (bo.getType().equals(4)) {// 手机号绑定
            phoneMatches(bo.getPhone(), 1);
            Map<String, String> collect = tripartiteUserVo.stream().collect(Collectors.toMap(TripartiteUserVo::getPhone, TripartiteUserVo::getPhone));
            if (collect.get(bo.getPhone()) != null && !collect.get(bo.getPhone()).equals(bo.getUuid()))
                throw new ServiceException("手机号已被使用");
            int update = baseMapper.update(null, new UpdateWrapper<TripartiteUser>()
                    .set("phone", bo.getPhone()).eq("uuid", bo.getUuid()));
            return update > 0;
        }
        return false;
    }

    @Override
    public AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "gitee":
                authRequest = new AuthGiteeRequest(AuthConfig.builder()
                        .clientId(giteePlatformConfig.getClientId())
                        .clientSecret(giteePlatformConfig.getClientSecret())
                        .redirectUri(giteePlatformConfig.getRedirectUrl())
                        .build());
                break;
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId("")
                        .clientSecret("")
                        .redirectUri("")
                        .build());
                break;
            case "baidu":
                authRequest = new AuthBaiduRequest(AuthConfig.builder()
                        .clientId(baiDuPlatformConfig.getClientId())
                        .clientSecret(baiDuPlatformConfig.getClientSecret())
                        .redirectUri(baiDuPlatformConfig.getRedirectUrl())
                        .build());
                break;
            case "weibo":
                authRequest = new AuthWeiboRequest(AuthConfig.builder()
                        .clientId(weiBoPlatformConfig.getClientId())
                        .clientSecret(weiBoPlatformConfig.getClientSecret())
                        .redirectUri(weiBoPlatformConfig.getRedirectUrl())
                        .build());
                break;
            case "zhifubao":
                authRequest = new AuthAlipayRequest(AuthConfig.builder()
                        .clientId(zhiFuBaoPlatformConfig.getClientId())
                        .clientSecret(zhiFuBaoPlatformConfig.getClientSecret())
                        .alipayPublicKey(zhiFuBaoPlatformConfig.getAlipayPublicKey())
                        .redirectUri(zhiFuBaoPlatformConfig.getRedirectUrl())
                        .build());
                break;

            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }

    @Override
    public UserInfoIdNameDto getLoginUser() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (loginUser == null) return null;
        return new UserInfoIdNameDto(loginUser.getUserId().toString(), loginUser.getUsername());
    }

    @Override
    public UserInfoIdNameDto getTripartiteUser() {
        TripartiteUser tripartiteUser = LoginHelper.getTripartiteUser();
        if (tripartiteUser == null) return null;
        return new UserInfoIdNameDto(tripartiteUser.getUuid(), tripartiteUser.getUsername());
    }
}
