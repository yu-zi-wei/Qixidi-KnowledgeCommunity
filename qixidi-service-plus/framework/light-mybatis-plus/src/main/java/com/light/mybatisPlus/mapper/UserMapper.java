package com.light.mybatisPlus.mapper;

import com.light.mybatisPlus.domain.entity.SysMenuHidden;
import com.light.mybatisPlus.domain.vo.SysMenuHiddenVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author zi-wei
 * @create 2025/2/5 17:04
 */
@Mapper
public interface UserMapper extends BaseMapperPlus<UserMapper, SysMenuHidden, SysMenuHiddenVo> {

    List<SysMenuHidden> selectListss();

}
