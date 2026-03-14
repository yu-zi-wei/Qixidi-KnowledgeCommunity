package com.qixidi.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import com.qixidi.auth.domain.entity.SysDept;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qixidi.system.domain.entity.SysRoleDept;
import com.qixidi.system.mapper.SysDeptMapper;
import com.qixidi.system.mapper.SysRoleDeptMapper;
import com.qixidi.system.service.ISysDataScopeService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据权限 实现
 * <p>
 * 注意: 此Service内不允许调用标注`数据权限`注解的方法
 * 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
 *
 * @author Lion Li
 */
@RequiredArgsConstructor
@Service("sdss")
public class SysDataScopeServiceImpl implements ISysDataScopeService {

    @Mapper
    private SysRoleDeptMapper roleDeptMapper;
    @Mapper
    private SysDeptMapper deptMapper;

    @Override
    public String getRoleCustom(Long roleId) {
        List<SysRoleDept> list = roleDeptMapper.selectList(
                new LambdaQueryWrapper<SysRoleDept>()
                        .select(SysRoleDept::getDeptId)
                        .eq(SysRoleDept::getRoleId, roleId));
        if (CollUtil.isNotEmpty(list)) {
            return list.stream().map(rd -> Convert.toStr(rd.getDeptId())).collect(Collectors.joining(","));
        }
        return null;
    }

    @Override
    public String getDeptAndChild(Long deptId) {
//        List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
//                .select(SysDept::getDeptId)
//                .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
//        List<Long> ids = deptList.stream().map(SysDept::getDeptId).collect(Collectors.toList());
//        ids.add(deptId);
//        List<SysDept> list = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
//                .select(SysDept::getDeptId)
//                .in(SysDept::getDeptId, ids));
//        if (CollUtil.isNotEmpty(list)) {
//            return list.stream().map(d -> Convert.toStr(d.getDeptId())).collect(Collectors.joining(","));
//        }
        return null;
    }

}
