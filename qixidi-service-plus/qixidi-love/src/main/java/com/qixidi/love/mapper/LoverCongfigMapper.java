package com.qixidi.love.mapper;

import com.light.mybatisPlus.mapper.BaseMapperPlus;
import com.qixidi.love.domain.LoverConfig;
import com.qixidi.love.domain.vo.LoverConfigVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * 基本配置Mapper接口
 *
 * @author ziwei
 * @date 2022-12-02
 */
@Mapper
public interface LoverCongfigMapper extends BaseMapperPlus<LoverCongfigMapper, LoverConfig, LoverConfigVo> {

    @Select("select mail from lover_config order by id asc LIMIT 1")
    String selectMail();
    @Select("select mail,filings,security_filings,realm_name from lover_config order by id asc LIMIT 1")
    LoverConfigVo configInfo();
}
