package com.qixidi.business.mapper.shield;

import com.qixidi.business.domain.entity.shield.ToShieldWord;
import com.qixidi.business.domain.vo.shield.ToShieldWordVo;
import com.light.mybatisPlus.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 屏蔽词库Mapper接口
 *
 * @author aurora
 * @date 2023-02-20
 */
@Mapper
public interface ToShieldWordMapper extends BaseMapperPlus<ToShieldWordMapper, ToShieldWord, ToShieldWordVo> {

    @Select("select keyword from to_shield_word where state=0")
    List<String> selectKeyword();
}
