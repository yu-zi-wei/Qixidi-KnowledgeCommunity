package com.aurora.business.mapper.dictum;

import com.aurora.business.domain.bo.dictum.DictumInfoBo;
import com.aurora.business.domain.entity.dictum.DictumInfo;
import com.aurora.business.domain.vo.CountUserWebsiteVo;
import com.aurora.business.domain.vo.dictum.DictumInfoVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Update("update dictum_album set employ_sum=employ_sum+1 where id=#{albumId}")
    Integer addEmploy(@Param("albumId") Long albumId);

    @Update("update dictum_album set employ_sum=employ_sum-1 where id=#{albumId}")
    Integer deleteEmploy(@Param("albumId") Long albumId);

    List<DictumInfoVo> selectGroupId();

    List<DictumInfoVo> selectAlbumId();

    @Select("select label from dictum_info where label is not null and label!=''")
    List<DictumInfoVo> selectAuthorLabel();

    List<CountUserWebsiteVo> selectDictumInfo();

    @Select("select author from dictum_info where author is not null and author!=''")
    List<DictumInfoVo> selectAuthorAuthors();

}

