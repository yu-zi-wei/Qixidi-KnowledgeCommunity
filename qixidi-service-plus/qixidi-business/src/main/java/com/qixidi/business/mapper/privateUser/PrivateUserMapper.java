package com.qixidi.business.mapper.privateUser;

import com.qixidi.business.domain.bo.privateUser.PrivateUserBo;
import com.qixidi.business.domain.entity.privateUser.PrivateUser;
import com.qixidi.business.domain.vo.privateUser.PrivateUserVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 私信记录Mapper接口
 *
 * @author aurora
 * @date 2023-03-23
 */
@Mapper
public interface PrivateUserMapper extends BaseMapperPlus<PrivateUserMapper, PrivateUser, PrivateUserVo> {

    Page<PrivateUserVo> selectListXml(@Param("bo") PrivateUserBo bo, Page<Object> build);
}
