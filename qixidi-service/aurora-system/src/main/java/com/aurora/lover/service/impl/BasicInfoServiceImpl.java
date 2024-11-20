/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 14:58
 * @version 1.0
 */
package com.aurora.lover.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.BasicInfo;
import com.aurora.lover.domain.bo.BasicInfoBo;
import com.aurora.lover.domain.vo.BasicInfoVo;
import com.aurora.lover.mapper.BasicInfoMapper;
import com.aurora.lover.service.IBasicInfoService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 网站基本信息Service业务层处理
 *
 * @author ruoyi
 * @date 2022-11-21
 */
@DS("slave_lover")
@RequiredArgsConstructor
@Service
public class BasicInfoServiceImpl implements IBasicInfoService {

    private final BasicInfoMapper baseMapper;

    /**
     * 查询网站基本信息
     */
    @Override
    public BasicInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询网站基本信息列表
     */
    @Override
    public TableDataInfo<BasicInfoVo> queryPageList(BasicInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<BasicInfo> lqw = buildQueryWrapper(bo);
        Page<BasicInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询网站基本信息列表
     */
    @Override
    public List<BasicInfoVo> queryList(BasicInfoBo bo) {
        LambdaQueryWrapper<BasicInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<BasicInfo> buildQueryWrapper(BasicInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<BasicInfo> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getTitle()), BasicInfo::getTitle, bo.getTitle());
        lqw.like(StringUtils.isNotBlank(bo.getIntroduce()), BasicInfo::getIntroduce, bo.getIntroduce());
        lqw.eq(StringUtils.isNotBlank(bo.getLeftImg()), BasicInfo::getLeftImg, bo.getLeftImg());
        lqw.like(StringUtils.isNotBlank(bo.getLeftName()), BasicInfo::getLeftName, bo.getLeftName());
        lqw.eq(StringUtils.isNotBlank(bo.getRightImg()), BasicInfo::getRightImg, bo.getRightImg());
        lqw.eq(bo.getState() != null, BasicInfo::getState, bo.getState());
        lqw.eq(bo.getType() != null, BasicInfo::getType, bo.getType());
        lqw.like(StringUtils.isNotBlank(bo.getRightName()), BasicInfo::getRightName, bo.getRightName());
        lqw.orderByDesc(BasicInfo::getId);
        return lqw;
    }

    /**
     * 新增网站基本信息
     */
    @Override
    public Boolean insertByBo(BasicInfoBo bo) {
        BasicInfo add = BeanUtil.toBean(bo, BasicInfo.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改网站基本信息
     */
    @Override
    public Boolean updateByBo(BasicInfoBo bo) {
        BasicInfo update = BeanUtil.toBean(bo, BasicInfo.class);
        update.setUpdateTime(new Date());
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除网站基本信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public BasicInfoVo basicInfoApi(BasicInfoBo bo) {
        return baseMapper.selectApi();
    }
}
