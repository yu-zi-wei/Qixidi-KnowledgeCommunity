package com.aurora.business.service.impl;

import com.aurora.business.domain.vo.SystemTaskConfigVo;
import com.aurora.business.mapper.SystemTaskConfigMapper;
import com.aurora.business.service.SystemTaskConfigService;
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
