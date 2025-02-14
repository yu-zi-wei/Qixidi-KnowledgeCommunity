/**
 * TODO 类描述
 *
 * @author: chenguangde
 * @createTime: 2022年11月20日 16:51
 * @version 1.0
 */
package com.qixidi.love.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.Album;
import com.qixidi.love.domain.vo.AlbumVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 时光相册Mapper接口
 *
 * @author ziwei
 * @date 2022-11-20
 */
@Mapper
public interface AlbumMapper extends BaseMapperPlus<AlbumMapper, Album, AlbumVo> {

}
