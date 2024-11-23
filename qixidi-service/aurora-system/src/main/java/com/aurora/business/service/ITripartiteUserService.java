package com.aurora.business.service;


import com.aurora.business.domain.bo.user.TripartiteUserBo;
import com.aurora.business.domain.bo.user.UserBindBo;
import com.aurora.business.domain.bo.user.UserInfoBo;
import com.aurora.business.domain.entity.count.CountUserWebsiteEntity;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.business.domain.vo.user.UserSimpleInfoVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.R;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.model.LoginUserMain;
import com.aurora.common.core.domain.model.PhoneBinding;
import com.aurora.common.core.domain.model.RegisterUserMain;
import com.aurora.common.core.page.TableDataInfo;
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

    R sendPhoneCode(String phone, String mag) throws Exception;

    Boolean phoneNumberBinding(PhoneBinding phoneBinding);

    Boolean bindEmail(UserBindBo bo);

   AuthRequest getAuthRequest(String source);
}
