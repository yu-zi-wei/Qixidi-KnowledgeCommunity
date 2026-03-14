package com.qixidi.business.mapper.user;

import com.qixidi.business.domain.entity.user.UserOrder;
import com.qixidi.business.domain.vo.user.UserOrderVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户订单Mapper接口
 *
 * @author aurora
 * @date 2023-04-04
 */
@Mapper
public interface UserOrderMapper extends BaseMapperPlus<UserOrderMapper, UserOrder, UserOrderVo> {

    Page<UserOrderVo> selectVoPageXml(Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<UserOrder> lqw);
}

