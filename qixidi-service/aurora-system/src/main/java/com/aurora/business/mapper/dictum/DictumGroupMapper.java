package com.aurora.business.mapper.dictum;

import com.aurora.business.domain.bo.dictum.DictumGroupBo;
import com.aurora.business.domain.entity.dictum.DictumGroup;
import com.aurora.business.domain.vo.dictum.DictumGroupVo;
import com.aurora.business.domain.vo.dictum.DictumInfoVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 名言分组Mapper接口
 *
 * @author aurora
 * @date 2023-04-24
 */
@Mapper
public interface DictumGroupMapper extends BaseMapperPlus<DictumGroupMapper, DictumGroup, DictumGroupVo> {

    IPage<DictumGroupVo> selectVoPageXml(@Param("bo") DictumGroupBo bo, Page<Object> build);

    @Update("update dictum_group set employ_sum=employ_sum+1 where id=#{id}")
    Integer addEmploySum(@Param("id") Long groupId);

    @Update("update dictum_group set employ_sum=employ_sum-1 where id=#{id}")
    Integer deleteEmploySum(@Param("id") Long groupId);

    Integer updateList(@Param("list") List<DictumInfoVo> dictumInfoVos);
}

