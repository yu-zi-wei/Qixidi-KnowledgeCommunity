package com.qixidi.business.service;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.auth.domain.entity.TripartiteUser;
import com.qixidi.auth.domain.model.LoginUserMain;
import com.qixidi.auth.domain.model.PhoneBinding;
import com.qixidi.auth.domain.model.RegisterUserMain;
import com.qixidi.business.domain.bo.user.TripartiteUserBo;
import com.qixidi.business.domain.bo.user.UserBindBo;
import com.qixidi.business.domain.bo.user.UserInfoBo;
import com.qixidi.business.domain.entity.count.CountUserWebsiteEntity;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
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

    Map isLogin();

    void frontDeskLogin(LoginUserMain loginUserMain);

    R register(RegisterUserMain registerUserMain);

    TripartiteUserVo websiteInfo();

    TripartiteUserVo getWebsiteInfo(String uuid);

    boolean updateUserAvatar(String tripartiteUuid, String avatar);

    boolean updateUserInfo(UserInfoBo bo);

    TripartiteUserVo BasicsUser();

    R sendOutCode(String email,String mag);

    List<TripartiteUserVo> fdUserList(UserInfoBo bo);

    CountUserWebsiteEntity fdUserData(String uuid);

    R resetPassword(RegisterUserMain registerUserMain);

    UserSimpleInfoVo isOnline(String userid);

    boolean accountCancellation(String uuid);

    R sendPhoneCode(String phone, String mag, HttpServletRequest request) throws Exception;

    Boolean phoneNumberBinding(PhoneBinding phoneBinding);

    Boolean bindEmail(UserBindBo bo);

   AuthRequest getAuthRequest(String source);
}
