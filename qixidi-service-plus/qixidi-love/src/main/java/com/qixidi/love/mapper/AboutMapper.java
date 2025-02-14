/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:20
 * @version 1.0
 */
package com.qixidi.love.mapper;


import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.About;
import com.qixidi.love.domain.vo.AboutVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 关于我们Mapper接口
 *
 * @author ziwei
 * @date 2022-11-30
 */
@Mapper
public interface AboutMapper extends BaseMapperPlus<AboutMapper, About, AboutVo> {

    @Select("SELECT * from  about where state=0 ORDER BY id asc LIMIT 1")
    AboutVo aboutApi();
}
