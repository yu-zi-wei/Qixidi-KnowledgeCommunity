package com.qixidi.business.mapper.dictum;

import com.qixidi.business.domain.bo.dictum.DictumAlbumBo;
import com.qixidi.business.domain.entity.dictum.DictumAlbum;
import com.qixidi.business.domain.vo.CountUserWebsiteVo;
import com.qixidi.business.domain.vo.dictum.DictumAlbumVo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 名言专辑Mapper接口
 *
 * @author aurora
 * @date 2023-04-24
 */
@Mapper
public interface DictumAlbumMapper extends BaseMapperPlus<DictumAlbumMapper, DictumAlbum, DictumAlbumVo> {

    IPage<DictumAlbumVo> selectVoPageXml(@Param("bo") DictumAlbumBo bo, Page<Object> build);

    Integer updateList(@Param("list") List<DictumInfoVo> albumId);

    List<CountUserWebsiteVo> selectAlbumTask();
}

