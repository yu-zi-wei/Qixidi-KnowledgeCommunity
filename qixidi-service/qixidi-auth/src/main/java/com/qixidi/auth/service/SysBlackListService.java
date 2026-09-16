package com.qixidi.auth.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.qixidi.auth.domain.entity.SysBlackList;
import com.qixidi.auth.mapper.SysBlackListMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * MP 3.5.17 移除 ServiceImpl，改为项目规范的构造器注入 Mapper 风格
 *
 * @author ziwei
 * @date 2024年09月16日
 */
@RequiredArgsConstructor
@Service
public class SysBlackListService {

    private final SysBlackListMapper baseMapper;

    public List<SysBlackList> list(Wrapper<SysBlackList> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    public boolean saveBatch(Collection<SysBlackList> entityList) {
        return baseMapper.insertBatch(entityList);
    }
}
