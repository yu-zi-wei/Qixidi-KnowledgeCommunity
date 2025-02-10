package com.light.mybatisPlus.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.light.mybatisPlus.domain.entity.SysMenuHidden;
import com.light.mybatisPlus.domain.vo.SysMenuHiddenVo;
import com.light.mybatisPlus.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zi-wei
 * @create 2025/2/5 18:51
 */
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<SysMenuHidden> selectList() {
//        List<SysMenuHidden> sysMenuHiddens = userMapper.selectList(new LambdaQueryWrapper<>());
//        List<SysMenuHiddenVo> sysMenuHiddenVos = userMapper.selectVoList(new LambdaQueryWrapper<>());
//        System.out.printf("sysMenuHiddens:" + sysMenuHiddens);
//        System.out.printf("sysMenuHiddenVos:" + sysMenuHiddenVos);
        return userMapper.selectListss();
//        return null;
    }
}
