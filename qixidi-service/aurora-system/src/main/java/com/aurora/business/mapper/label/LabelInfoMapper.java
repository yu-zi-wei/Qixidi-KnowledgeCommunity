package com.aurora.business.mapper.label;

import com.aurora.business.domain.bo.label.LabelInfoBo;
import com.aurora.business.domain.entity.label.LabelInfo;
import com.aurora.business.domain.vo.label.LabelInfoVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 标签信息Mapper接口
 *
 * @author aurora
 * @date 2022-08-16
 */
@Mapper
public interface LabelInfoMapper extends BaseMapperPlus<LabelInfoMapper, LabelInfo, LabelInfoVo> {
    Integer updateAddFollow(@Param("id") String id);

    Integer updateDeleteFollow(@Param("id") String id);

    Page<LabelInfoVo> selectVoPages(@Param("bo") LabelInfoBo bo, Page<LabelInfo> build);

    @Select("select id,label_name from label_info where `state`=0")
    List<LabelInfoVo> selectSimpleList();

    int updateTaskList(@Param("list") List<LabelInfoVo> list1);
}

