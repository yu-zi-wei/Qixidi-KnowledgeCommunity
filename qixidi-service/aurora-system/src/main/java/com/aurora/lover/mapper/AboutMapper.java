/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:20
 * @version 1.0
 */
package com.aurora.lover.mapper;


import com.aurora.common.core.mapper.BaseMapperPlus;
import com.aurora.lover.domain.About;
import com.aurora.lover.domain.vo.AboutVo;
import org.apache.ibatis.annotations.Select;

/**
 * 关于我们Mapper接口
 *
 * @author ziwei
 * @date 2022-11-30
 */
public interface AboutMapper extends BaseMapperPlus<AboutMapper, About, AboutVo> {

    @Select("SELECT * from  about where state=0 ORDER BY id asc LIMIT 1")
    AboutVo aboutApi();
}
