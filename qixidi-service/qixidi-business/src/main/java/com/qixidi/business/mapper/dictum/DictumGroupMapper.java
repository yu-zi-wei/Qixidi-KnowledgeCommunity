package com.qixidi.business.mapper.dictum;

import com.qixidi.business.domain.bo.dictum.DictumGroupBo;
import com.qixidi.business.domain.entity.dictum.DictumGroup;
import com.qixidi.business.domain.vo.dictum.DictumGroupVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 名言分组Mapper接口
 *
 * @author aurora
 * @date 2023-04-24
 */
@Mapper
public interface DictumGroupMapper extends BaseMapperPlus<DictumGroupMapper, DictumGroup, DictumGroupVo> {

    IPage<DictumGroupVo> selectVoPageXml(@Param("bo") DictumGroupBo bo, Page<Object> build);
}
