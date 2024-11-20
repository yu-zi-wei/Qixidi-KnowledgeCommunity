package com.aurora.business.mapper.shield;

import com.aurora.business.domain.entity.shield.ToShieldWord;
import com.aurora.business.domain.vo.shield.ToShieldWordVo;
import com.aurora.common.core.mapper.BaseMapperPlus;
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
