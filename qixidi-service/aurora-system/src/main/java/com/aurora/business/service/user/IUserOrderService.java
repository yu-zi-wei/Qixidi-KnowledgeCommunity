package com.aurora.business.service.user;

import com.aurora.business.domain.bo.user.UserOrderBo;
import com.aurora.business.domain.vo.user.UserOrderVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户订单Service接口
 *
 * @author aurora
 * @date 2023-04-04
 */
public interface IUserOrderService {

    /**
     * 查询用户订单
     *
     * @param id 用户订单主键
     * @return 用户订单
     */
    UserOrderVo queryById(Long id);

    /**
     * 查询用户订单列表
     *
     * @param bo 用户订单
     * @return 用户订单集合
     */
    TableDataInfo<UserOrderVo> queryPageList(UserOrderBo bo, PageQuery pageQuery);

    /**
     * 查询用户订单列表
     *
     * @param bo 用户订单
     * @return 用户订单集合
     */
    List<UserOrderVo> queryList(UserOrderBo bo);

    /**
     * 修改用户订单
     *
     * @param bo 用户订单
     * @return 结果
     */
    Boolean insertByBo(UserOrderBo bo);

    /**
     * 修改用户订单
     *
     * @param bo 用户订单
     * @return 结果
     */
    Boolean updateByBo(UserOrderBo bo);

    /**
     * 校验并批量删除用户订单信息
     *
     * @param ids 需要删除的用户订单主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean deleteOrder(Long id);
}

