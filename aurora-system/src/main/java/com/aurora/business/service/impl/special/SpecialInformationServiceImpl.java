package com.aurora.business.service.impl.special;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.special.SpecialInformationBo;
import com.aurora.business.domain.entity.special.SpecialInformation;
import com.aurora.business.domain.vo.special.SpecialInformationVo;
import com.aurora.business.mapper.special.SpecialInformationMapper;
import com.aurora.business.service.special.ISpecialInformationService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.helper.LoginHelper;
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
 * 专栏信息Service业务层处理
 *
 * @author aurora
 * @date 2022-08-19
 */
@RequiredArgsConstructor
@Service
public class SpecialInformationServiceImpl implements ISpecialInformationService {

    private final SpecialInformationMapper baseMapper;

    /**
     * 查询专栏信息
     *
     * @param id 专栏信息主键
     * @return 专栏信息
     */
    @Override
    public SpecialInformationVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询专栏信息列表
     *
     * @param bo 专栏信息
     * @return 专栏信息
     */
    @Override
    public TableDataInfo<SpecialInformationVo> queryPageList(SpecialInformationBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<SpecialInformation> lqw = buildQueryWrapper(bo);
        Page<SpecialInformationVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询专栏信息列表
     *
     * @param bo 专栏信息
     * @return 专栏信息
     */
    @Override
    public List<SpecialInformationVo> queryList(SpecialInformationBo bo) {
        LambdaQueryWrapper<SpecialInformation> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<SpecialInformation> buildQueryWrapper(SpecialInformationBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SpecialInformation> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getSpecialName()), SpecialInformation::getSpecialName, bo.getSpecialName());
        lqw.eq(StringUtils.isNotBlank(bo.getSpecialIntroduce()), SpecialInformation::getSpecialIntroduce, bo.getSpecialIntroduce());
        lqw.eq(bo.getStatus() != null, SpecialInformation::getStatus, bo.getStatus());
        lqw.eq(bo.getUid() != null, SpecialInformation::getUid, bo.getUid());
        lqw.eq(bo.getUpdateId() != null, SpecialInformation::getUpdateId, bo.getUpdateId());
        return lqw;
    }

    /**
     * 新增专栏信息
     *
     * @param bo 专栏信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(SpecialInformationBo bo) {
        SpecialInformation add = BeanUtil.toBean(bo, SpecialInformation.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改专栏信息
     *
     * @param bo 专栏信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(SpecialInformationBo bo) {
        SpecialInformation update = BeanUtil.toBean(bo, SpecialInformation.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     *
     * @param entity 实体类数据
     */
    private void validEntityBeforeSave(SpecialInformation entity) {
        entity.setUid(LoginHelper.getUserId());
        entity.setCreateTime(new Date());
    }

    /**
     * 批量删除专栏信息
     *
     * @param ids 需要删除的专栏信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}

