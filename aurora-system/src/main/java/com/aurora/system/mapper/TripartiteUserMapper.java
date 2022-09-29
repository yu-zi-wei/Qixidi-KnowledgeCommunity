package com.aurora.system.mapper;

import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.business.domain.vo.user.TripartiteUserVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

/**
 * 【请填写功能名称】Mapper接口
 *
 * @author ruoyi
 * @date 2022-06-12
 */
public interface TripartiteUserMapper extends BaseMapperPlus<TripartiteUserMapper, TripartiteUser, TripartiteUserVo> {

    TripartiteUserVo selectWebInfo(@Param("uuid") String tripartiteUuid);
}
