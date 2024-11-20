package com.aurora.business.mapper.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateUserBo;
import com.aurora.business.domain.entity.privateUser.PrivateUser;
import com.aurora.business.domain.vo.privateUser.PrivateUserVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 私信记录Mapper接口
 *
 * @author aurora
 * @date 2023-03-23
 */
public interface PrivateUserMapper extends BaseMapperPlus<PrivateUserMapper, PrivateUser, PrivateUserVo> {

    Page<PrivateUserVo> selectListXml(@Param("bo") PrivateUserBo bo, Page<Object> build);
}
