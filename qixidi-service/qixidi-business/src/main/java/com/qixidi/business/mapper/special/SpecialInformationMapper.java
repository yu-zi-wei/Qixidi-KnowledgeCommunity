package com.qixidi.business.mapper.special;

import com.qixidi.business.domain.entity.special.SpecialInformation;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.special.SpecialInformationVo;
import com.light.core.core.domain.CensusEntity;
import com.light.core.core.domain.vo.CensusVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专栏信息Mapper接口
 *
 * @author aurora
 * @date 2022-08-19
 */
@Mapper
public interface SpecialInformationMapper extends BaseMapperPlus<SpecialInformationMapper, SpecialInformation, SpecialInformationVo> {

    Page<SpecialInformationVo> selectUserName(Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<SpecialInformation> lqw);

    List<CensusVo> timeSpecialCensus(@Param("bo")CensusEntity bo);

    List<CountUserWebsiteVo> selectSpecialTask();
}
