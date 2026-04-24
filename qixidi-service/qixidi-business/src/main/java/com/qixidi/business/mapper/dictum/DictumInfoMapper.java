package com.qixidi.business.mapper.dictum;

import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.entity.dictum.DictumInfo;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 名言信息Mapper接口
 *
 * @author aurora
 * @date 2023-04-24
 */
@Mapper
public interface DictumInfoMapper extends BaseMapperPlus<DictumInfoMapper, DictumInfo, DictumInfoVo> {

    IPage<DictumInfoVo> selectVoPageXml(@Param("bo") DictumInfoBo bo, Page<Object> build);

    @Select("select label from b_dictum_info where label is not null and label!='' and `state`=0")
    List<DictumInfoVo> selectAuthorLabel();

    List<CountUserWebsiteVo> selectDictumCountByUserIds(@Param("uuids") java.util.Collection<String> uuids);

    @Select("select author from b_dictum_info where author is not null and author!='' and `state`=0")
    List<DictumInfoVo> selectAuthorAuthors();

}
