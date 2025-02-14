/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 16:52
 * @version 1.0
 */
package com.qixidi.love.mapper;


import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.LoverRecord;
import com.qixidi.love.domain.vo.LoverRecordVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 爱情记录Mapper接口
 *
 * @author ziwei
 * @date 2022-11-21
 */
@Mapper
public interface LoverRecordMapper extends BaseMapperPlus<LoverRecordMapper, LoverRecord, LoverRecordVo> {

}
