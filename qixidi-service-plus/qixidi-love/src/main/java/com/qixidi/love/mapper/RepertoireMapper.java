/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月22日 15:56
 * @version 1.0
 */
package com.qixidi.love.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.Repertoire;
import com.qixidi.love.domain.vo.RepertoireVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 爱情清单Mapper接口
 *
 * @author ziwei
 * @date 2022-11-22
 */
@Mapper
public interface RepertoireMapper extends BaseMapperPlus<RepertoireMapper, Repertoire, RepertoireVo> {

}
