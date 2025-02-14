/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:14
 * @version 1.0
 */
package com.qixidi.love.mapper;


import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.LoverCarousel;
import com.qixidi.love.domain.vo.LoverCarouselVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 爱情轮播图Mapper接口
 *
 * @author ziwei
 * @date 2022-11-29
 */
@Mapper
public interface LoverCarouselMapper extends BaseMapperPlus<LoverCarouselMapper, LoverCarousel, LoverCarouselVo> {

    @Select("SELECT img, introduce, type, create_time FROM lover_carousel WHERE type =( SELECT min( type ) FROM lover_carousel WHERE state = 0 ) and state = 0\n" +
        "LIMIT 0,\n" +
        "#{size}")
    List<LoverCarouselVo> carouselApi(@Param("size") long size);
}
