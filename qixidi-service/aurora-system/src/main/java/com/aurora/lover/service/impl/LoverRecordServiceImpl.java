/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 16:57
 * @version 1.0
 */
package com.aurora.lover.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.LoverRecord;
import com.aurora.lover.domain.bo.LoverRecordBo;
import com.aurora.lover.domain.vo.LoverRecordVo;
import com.aurora.lover.mapper.LoverRecordMapper;
import com.aurora.lover.service.ILoverRecordService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 爱情记录Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-21
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class LoverRecordServiceImpl implements ILoverRecordService {

    private final LoverRecordMapper baseMapper;

    /**
     * 查询爱情记录
     */
    @Override
    public LoverRecordVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询爱情记录列表
     */
    @Override
    public TableDataInfo<LoverRecordVo> queryPageList(LoverRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LoverRecord> lqw = buildQueryWrapper(bo);
        //不查询content字段
        lqw.select(LoverRecord.class, f -> !f.getColumn().equals("content"));
        Page<LoverRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询爱情记录列表
     */
    @Override
    public List<LoverRecordVo> queryList(LoverRecordBo bo) {
        LambdaQueryWrapper<LoverRecord> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<LoverRecord> buildQueryWrapper(LoverRecordBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<LoverRecord> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), LoverRecord::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getContent()), LoverRecord::getContent, bo.getContent());
        lqw.eq(bo.getState() != null, LoverRecord::getState, bo.getState());
        lqw.like(StringUtils.isNotBlank(bo.getAddress()), LoverRecord::getAddress, bo.getAddress());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            LoverRecord::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.orderByDesc(LoverRecord::getId);
        return lqw;
    }

    /**
     * 新增爱情记录
     */
    @Override
    public Boolean insertByBo(LoverRecordBo bo) {
        LoverRecord add = BeanUtil.toBean(bo, LoverRecord.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改爱情记录
     */
    @Override
    public Boolean updateByBo(LoverRecordBo bo) {
        LoverRecord update = BeanUtil.toBean(bo, LoverRecord.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除爱情记录
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<LoverRecordVo> loverRecordApi(LoverRecordBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<LoverRecord> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), LoverRecord::getTitle, bo.getTitle());
        lqw.eq(true, LoverRecord::getState, bo.getState());
        lqw.orderByDesc(LoverRecord::getCreateTime);
        Page<LoverRecordVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }
}
