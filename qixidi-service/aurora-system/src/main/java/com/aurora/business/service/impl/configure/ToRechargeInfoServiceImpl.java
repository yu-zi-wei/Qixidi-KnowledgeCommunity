package com.aurora.business.service.impl.configure;

import cn.hutool.core.bean.BeanUtil;
import com.aurora.business.domain.bo.configure.ToRechargeInfoBo;
import com.aurora.business.domain.entity.configure.ToRechargeInfo;
import com.aurora.business.domain.vo.configure.ToRechargeInfoVo;
import com.aurora.business.mapper.configure.ToRechargeInfoMapper;
import com.aurora.business.service.configure.IToRechargeInfoService;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.common.utils.DateUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 充值信息Service业务层处理
 *
 * @author aurora
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
public class ToRechargeInfoServiceImpl implements IToRechargeInfoService {

    private final ToRechargeInfoMapper baseMapper;

    /**
     * 查询充值信息
     *
     * @param id 充值信息主键
     * @return 充值信息
     */
    @Override
    public ToRechargeInfoVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询充值信息列表
     *
     * @param bo 充值信息
     * @return 充值信息
     */
    @Override
    public TableDataInfo<ToRechargeInfoVo> queryPageList(ToRechargeInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ToRechargeInfo> lqw = buildQueryWrapper(bo);
        Page<ToRechargeInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询充值信息列表
     *
     * @param bo 充值信息
     * @return 充值信息
     */
    @Override
    public List<ToRechargeInfoVo> queryList(ToRechargeInfoBo bo) {
        LambdaQueryWrapper<ToRechargeInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<ToRechargeInfo> buildQueryWrapper(ToRechargeInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<ToRechargeInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMoney() != null, ToRechargeInfo::getMoney, bo.getMoney());
        lqw.eq(bo.getCurrency() != null, ToRechargeInfo::getCurrency, bo.getCurrency());
        lqw.eq(bo.getIsDiscount() != null, ToRechargeInfo::getIsDiscount, bo.getIsDiscount());
        lqw.eq(bo.getDiscount() != null, ToRechargeInfo::getDiscount, bo.getDiscount());
        lqw.eq(bo.getStartTime() != null, ToRechargeInfo::getStartTime, bo.getStartTime());
        lqw.eq(bo.getEndTime() != null, ToRechargeInfo::getEndTime, bo.getEndTime());
        lqw.eq(bo.getMemberDiscount() != null, ToRechargeInfo::getMemberDiscount, bo.getMemberDiscount());
        lqw.orderByAsc(ToRechargeInfo::getMoney);
        return lqw;
    }

    /**
     * 新增充值信息
     *
     * @param bo 充值信息
     * @return 结果
     */
    @Override
    public Boolean insertByBo(ToRechargeInfoBo bo) {
        ToRechargeInfo add = BeanUtil.toBean(bo, ToRechargeInfo.class);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改充值信息
     *
     * @param bo 充值信息
     * @return 结果
     */
    @Override
    public Boolean updateByBo(ToRechargeInfoBo bo) {
        ToRechargeInfo update = BeanUtil.toBean(bo, ToRechargeInfo.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除充值信息
     *
     * @param ids 需要删除的充值信息主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public TableDataInfo<ToRechargeInfoVo> list(ToRechargeInfoBo bo) {
        List<ToRechargeInfoVo> toRechargeInfos = baseMapper.selectVoList(new QueryWrapper<ToRechargeInfo>()
            .orderByAsc("money"));
        TableDataInfo tableDataInfo = new TableDataInfo();
        toRechargeInfos.forEach(item -> {
            if (item.getIsDiscount() == 1
                && DateUtils.isEffectiveDate(new Date(), item.getStartTime(), item.getEndTime())) {
                item.setMoveMoney(
                    BigDecimal.valueOf(item.getMoney().intValue())
                        .multiply((BigDecimal.valueOf(item.getDiscount())).divide(BigDecimal.valueOf(10))));
                item.setIsFinish(false);
            }
            if (item.getMemberDiscount() > 0) {
                item.setMemberMoney(BigDecimal.valueOf(item.getMoney().intValue())
                    .multiply((BigDecimal.valueOf(item.getMemberDiscount())).divide(BigDecimal.valueOf(10))));
            }
        });
        tableDataInfo.setRows(toRechargeInfos);
        return tableDataInfo;
    }
}

