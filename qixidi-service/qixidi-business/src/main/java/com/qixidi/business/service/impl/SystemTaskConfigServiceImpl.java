package com.qixidi.business.service.impl;

import com.qixidi.business.domain.vo.SystemTaskConfigVo;
import com.qixidi.business.mapper.SystemTaskConfigMapper;
import com.qixidi.business.service.SystemTaskConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author zi-wei
 * @create 2024/11/17 14:48
 */
@Service
public class SystemTaskConfigServiceImpl implements SystemTaskConfigService {
    @Autowired
    private SystemTaskConfigMapper baseMapper;

    @Override
    public List<SystemTaskConfigVo> list() {
        return baseMapper.selectVoList(null);
    }
}
