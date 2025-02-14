/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 14:53
 * @version 1.0
 */
package com.qixidi.love.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.BasicInfo;
import com.qixidi.love.domain.vo.BasicInfoVo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 网站基本信息Mapper接口
 *
 * @author ziwei
 * @date 2022-11-21
 */
@Mapper
public interface BasicInfoMapper extends BaseMapperPlus<BasicInfoMapper, BasicInfo, BasicInfoVo> {

    BasicInfoVo selectApi();

}
