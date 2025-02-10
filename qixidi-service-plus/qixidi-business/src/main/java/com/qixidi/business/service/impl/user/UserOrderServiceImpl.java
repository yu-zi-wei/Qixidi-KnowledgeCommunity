package com.qixidi.business.service.impl.user;

import cn.hutool.core.bean.BeanUtil;
import com.qixidi.business.domain.bo.user.UserOrderBo;
import com.qixidi.business.domain.entity.user.UserOrder;
import com.qixidi.business.domain.vo.user.UserOrderVo;
import com.qixidi.business.mapper.user.UserOrderMapper;
import com.qixidi.business.service.user.IUserOrderService;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.light.core.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户订单Service业务层处理
 *
 * @author aurora
 * @date 2023-04-04
 */
@RequiredArgsConstructor
@Service
public class UserOrderServiceImpl implements IUserOrderService {

    private final UserOrderMapper baseMapper;

    /**
     * 查询用户订单
     *
     * @param id 用户订单主键
     * @return 用户订单
     */
    @Override
    public UserOrderVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户订单列表
     *
     * @param bo 用户订单
     * @return 用户订单
     */
    @Override
    public TableDataInfo<UserOrderVo> queryPageList(UserOrderBo bo, PageQuery pageQuery) {
        QueryWrapper<UserOrder> lqw = buildQueryWrapper(bo);
        Page<UserOrderVo> result = baseMapper.selectVoPageXml(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户订单列表
     *
     * @param bo 用户订单
     * @return 用户订单
     */
    @Override
    public List<UserOrderVo> queryList(UserOrderBo bo) {
        QueryWrapper<UserOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private QueryWrapper<UserOrder> buildQueryWrapper(UserOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        QueryWrapper<UserOrder> lqw = Wrappers.query();
        lqw.eq(StringUtils.isNotBlank(bo.getUid()), "uo.uid", bo.getUid());
        lqw.like(StringUtils.isNotBlank(bo.getNickname()), " um.nickname", bo.getNickname());
        lqw.eq(bo.getRechargeMoney() != null, "uo.recharge_money", bo.getRechargeMoney());
        lqw.eq(bo.getDiscount() != null, "uo.discount", bo.getDiscount());
        lqw.eq(StringUtils.isNotBlank(bo.getPaymentMethod()), "uo.payment_method", bo.getPaymentMethod());
        lqw.eq(bo.getPaymentState() != null, "uo.payment_state", bo.getPaymentState());
        lqw.orderByDesc("uo.create_time");
        return lqw;
    }

    /**
     * 新增用户订单
     *
     * @param bo 用户订单
     * @return 结果
     */
    @Override
    public Boolean insertByBo(UserOrderBo bo) {
        UserOrder add = BeanUtil.toBean(bo, UserOrder.class);
        add.setCreateTime(new Date());
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户订单
     *
     * @param bo 用户订单
     * @return 结果
     */
    @Override
    public Boolean updateByBo(UserOrderBo bo) {
        UserOrder update = BeanUtil.toBean(bo, UserOrder.class);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 批量删除用户订单
     *
     * @param ids 需要删除的用户订单主键
     * @return 结果
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean deleteOrder(Long id) {
        return baseMapper.deleteById(id) > 0;
    }
}

