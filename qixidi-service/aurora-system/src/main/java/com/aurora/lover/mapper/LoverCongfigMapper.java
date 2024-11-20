package com.aurora.lover.mapper;

import com.aurora.common.core.mapper.BaseMapperPlus;
import com.aurora.lover.domain.LoverConfig;
import com.aurora.lover.domain.vo.LoverConfigVo;
import org.apache.ibatis.annotations.Select;

/**
 * 基本配置Mapper接口
 *
 * @author ruoyi
 * @date 2022-12-02
 */
public interface LoverCongfigMapper extends BaseMapperPlus<LoverCongfigMapper, LoverConfig, LoverConfigVo> {

    @Select("select mail from lover_config order by id asc LIMIT 1")
    String selectMail();
    @Select("select mail,filings,security_filings,realm_name from lover_config order by id asc LIMIT 1")
    LoverConfigVo configInfo();
}
