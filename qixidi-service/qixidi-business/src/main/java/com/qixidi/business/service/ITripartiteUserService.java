package com.qixidi.business.service;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.model.LoginUserMain;
import com.qixidi.auth.domain.model.PhoneBinding;
import com.qixidi.auth.domain.model.RegisterUserMain;
import com.qixidi.business.domain.bo.user.CreatorApplicationBo;
import com.qixidi.business.domain.bo.user.TripartiteUserBo;
import com.qixidi.business.domain.bo.user.UserBindBo;
import com.qixidi.business.domain.bo.user.UserInfoBo;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.qixidi.business.domain.vo.user.UserLoginStatusVo;
import com.qixidi.business.domain.vo.user.UserSimpleInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import me.zhyd.oauth.request.AuthRequest;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 平台用户Service接口
 *
 * @author ziwei
 * @date 2022-06-12
 */
public interface ITripartiteUserService {

    /**
     * 查询平台用户
     *
     * @param uuid 平台用户主键
     * @return 平台用户
     */
    TripartiteUserVo queryById(String uuid);

    /**
     * 按 uid 查用户基本信息（昵称/头像等展示字段，单表轻查，不分状态）
     *
     * @param uid 用户 uuid
     * @return 用户（不存在返回 null）
     */
    TripartiteUserVo getUserByUid(String uid);

    /**
     * 按 uid 集合批量查用户基本信息（跨业务域展示字段组装唯一入口，不分页、不分状态）
     *
     * @param uids 用户 uuid 集合
     * @return 用户列表（uid 悬空的不在结果里）
     */
    List<TripartiteUserVo> getUserByUids(Collection<String> uids);

    /**
     * 按昵称模糊查用户 uuid 集合（跨业务域"作者名称搜索"唯一入口：先查 id 列表再带入业务表 in，不分状态）
     *
     * @param nickname 昵称关键词
     * @return 命中用户的 uuid 列表（无命中返回空集合，调用方须短路返回空页）
     */
    List<String> getUidsByNickname(String nickname);

    /**
     * 查存活用户（state=0）uuid → 用户信息映射（跨业务域"存活作者过滤"唯一入口）
     *
     * @return uuid → TripartiteUserVo（空表/无存活用户返回空 Map，调用方须短路返回空页）
     */
    Map<String, TripartiteUserVo> getAliveUserMap();

    /**
     * 查询平台用户列表
     *
     * @return 平台用户集合
     */
    TableDataInfo<TripartiteUserVo> queryPageList(TripartiteUserBo bo, PageQuery pageQuery);

    /**
     * 查询平台用户列表
     *
     * @return 平台用户集合
     */
    List<TripartiteUserVo> queryList(TripartiteUserBo bo);

    /**
     * 修改平台用户
     *
     * @return 结果
     */
    Boolean insertByBo(TripartiteUserBo bo);

    /**
     * 修改平台用户
     *
     * @return 结果
     */
    Boolean updateByBo(TripartiteUserBo bo);

    /**
     * 校验并批量删除平台用户信息
     *
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    void oauthLogin(TripartiteUser tripartiteUser);

    UserLoginStatusVo isLogin();

    void frontDeskLogin(LoginUserMain loginUserMain);

    Integer register(RegisterUserMain registerUserMain);

    TripartiteUserVo websiteInfo();

    TripartiteUserVo getWebsiteInfo(String uuid);

    boolean updateUserAvatar(String tripartiteUuid, String avatar);

    boolean updateUserInfo(UserInfoBo bo);

    TripartiteUserVo BasicsUser();

    void sendOutCode(String email, Integer type);

    List<TripartiteUserVo> fdUserList(UserInfoBo bo);

    CountUserWebsiteVo fdUserData(String uuid);

    Integer resetPassword(RegisterUserMain registerUserMain);

    UserSimpleInfoVo isOnline(String userid);

    boolean accountCancellation(String uuid);

    void sendPhoneCode(String phone, Integer type, HttpServletRequest request) throws Exception;

    Boolean phoneNumberBinding(PhoneBinding phoneBinding);

    Boolean bindEmail(UserBindBo bo);

    AuthRequest getAuthRequest(String source);

    List<TripartiteUserVo> fdUserAllList();

    void creatorApplication(CreatorApplicationBo bo);

}
