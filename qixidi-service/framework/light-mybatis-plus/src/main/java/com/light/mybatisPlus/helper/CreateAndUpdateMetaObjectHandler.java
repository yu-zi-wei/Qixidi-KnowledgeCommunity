package com.light.mybatisPlus.helper;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.light.core.core.domain.BaseEntity;
import com.light.core.utils.spring.SpringUtils;
import com.light.exception.ServiceException;
import com.light.mybatisPlus.domain.dto.UserInfoIdNameDto;
import com.light.mybatisPlus.interfaces.UserInfoInterface;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * MP注入处理器
 *
 * @author Lion Li
 * @date 2021/4/25
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {


    private UserInfoInterface userInfoInterface;

    public CreateAndUpdateMetaObjectHandler() {
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                Date current = ObjectUtil.isNotNull(baseEntity.getCreateTime())
                        ? baseEntity.getCreateTime() : new Date();
                baseEntity.setCreateTime(current);
                baseEntity.setUpdateTime(current);
                String loginUsername = getLoginUsername();
                if (loginUsername != null) {
//                // 当前已登录 且 创建人为空 则填充
                    baseEntity.setCreateBy(loginUsername);
//                // 当前已登录 且 更新人为空 则填充
                    baseEntity.setUpdateBy(loginUsername);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            if (ObjectUtil.isNotNull(metaObject) && metaObject.getOriginalObject() instanceof BaseEntity) {
                BaseEntity baseEntity = (BaseEntity) metaObject.getOriginalObject();
                Date current = new Date();
                // 更新时间填充(不管为不为空)
                baseEntity.setUpdateTime(current);
                String loginUsername = getLoginUsername();
                if (loginUsername != null) {
//                // 当前已登录 且 更新人为空 则填充
                    baseEntity.setUpdateBy(loginUsername);
                }
            }
        } catch (Exception e) {
            throw new ServiceException("自动注入异常 => " + e.getMessage(), HttpStatus.HTTP_UNAUTHORIZED);
        }
    }

    public UserInfoInterface injectionUserInfoInterface() {
        if (userInfoInterface == null) {
            synchronized (this.getClass()) {
                if (userInfoInterface == null) {
                    userInfoInterface = SpringUtils.getBean(UserInfoInterface.class);
                }
            }
        }
        return userInfoInterface;
    }

    /**
     * 获取登录用户名
     */
    private String getLoginUsername() {
        UserInfoIdNameDto loginUser = injectionUserInfoInterface().getLoginUser();
        if (loginUser != null) {
            return loginUser.getUsername();
        }
        UserInfoIdNameDto tripartiteUser = userInfoInterface.getTripartiteUser();
        return tripartiteUser == null ? null : tripartiteUser.getUsername();
    }

    /**
     * 获取登录用户id
     */
    private String getLoginUserId() {
        UserInfoIdNameDto loginUser = injectionUserInfoInterface().getLoginUser();
        if (loginUser != null) {
            return loginUser.getUid();
        }
        UserInfoIdNameDto tripartiteUser = userInfoInterface.getTripartiteUser();
        return tripartiteUser == null ? null : tripartiteUser.getUid();
    }

}
