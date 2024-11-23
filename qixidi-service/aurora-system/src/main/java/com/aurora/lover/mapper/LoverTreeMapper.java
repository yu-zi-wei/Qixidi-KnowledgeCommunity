/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:58
 * @version 1.0
 */
package com.aurora.lover.mapper;

import com.aurora.common.core.mapper.BaseMapperPlus;
import com.aurora.lover.domain.LoverTree;
import com.aurora.lover.domain.vo.LoverTreeVo;
import org.apache.ibatis.annotations.Select;

/**
 * 爱情树Mapper接口
 *
 * @author ziwei
 * @date 2022-11-30
 */
public interface LoverTreeMapper extends BaseMapperPlus<LoverTreeMapper, LoverTree, LoverTreeVo> {

    @Select("SELECT * from  lover_tree where state=0 ORDER BY type desc LIMIT 1")
    LoverTreeVo loverTreeApi();
}
