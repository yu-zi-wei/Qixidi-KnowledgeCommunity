/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 14:53
 * @version 1.0
 */
package com.aurora.lover.mapper;

import com.aurora.common.core.mapper.BaseMapperPlus;
import com.aurora.lover.domain.BasicInfo;
import com.aurora.lover.domain.vo.BasicInfoVo;

/**
 * 网站基本信息Mapper接口
 *
 * @author ziwei
 * @date 2022-11-21
 */
public interface BasicInfoMapper extends BaseMapperPlus<BasicInfoMapper, BasicInfo, BasicInfoVo> {

    BasicInfoVo selectApi();

}
