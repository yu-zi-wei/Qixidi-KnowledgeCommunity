package com.aurora.business.mapper.label;


import com.aurora.business.domain.bo.label.LabelGroupingInfoBo;
import com.aurora.business.domain.entity.label.LabelGroupingInfo;
import com.aurora.business.domain.vo.label.LabelGroupingInfoVo;
import com.aurora.business.domain.vo.stat.StatDataInfoVo;
import com.aurora.business.domain.vo.stat.StatReturnDataVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 标签分组信息Mapper接口
 *
 * @author aurora
 * @date 2022-08-16
 */
@Mapper
public interface LabelGroupingInfoMapper extends BaseMapperPlus<LabelGroupingInfoMapper, LabelGroupingInfo, LabelGroupingInfoVo> {

    @Select("select grouping_name from label_grouping_info where id=#{groupingId}")
    String selectNameById(@Param("groupingId") Long groupingId);

    IPage<LabelGroupingInfoVo> fdkGroupingList(@Param("bo") LabelGroupingInfoBo bo, Page<LabelGroupingInfo> build);

    List<StatReturnDataVo> selectStatData(@Param("bo")StatDataInfoVo bo);
}
