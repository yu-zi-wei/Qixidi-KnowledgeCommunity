package com.qixidi.business.mapper.album;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.business.domain.entity.album.Album;
import com.qixidi.business.domain.vo.album.AlbumVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 相册
 *
 * @author zi-wei
 */
@Mapper
public interface AlbumMapper extends BaseMapperPlus<AlbumMapper, Album, AlbumVo> {
}
