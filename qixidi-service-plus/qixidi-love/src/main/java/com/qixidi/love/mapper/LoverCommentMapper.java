/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:30
 * @version 1.0
 */
package com.qixidi.love.mapper;


import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.LoverComment;
import com.qixidi.love.domain.vo.LoverCommentVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 爱情祝福语Mapper接口
 *
 * @author ziwei
 * @date 2022-11-30
 */
@Mapper
public interface LoverCommentMapper extends BaseMapperPlus<LoverCommentMapper, LoverComment, LoverCommentVo> {

}
