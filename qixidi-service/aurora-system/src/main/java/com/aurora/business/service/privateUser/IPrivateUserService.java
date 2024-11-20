package com.aurora.business.service.privateUser;

import com.aurora.business.domain.bo.privateUser.PrivateUserBo;
import com.aurora.business.domain.vo.privateUser.PrivateUserVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 私信记录Service接口
 *
 * @author aurora
 * @date 2023-03-23
 */
public interface IPrivateUserService {

    /**
     * 查询私信记录
     *
     * @param id 私信记录主键
     * @return 私信记录
     */
    PrivateUserVo queryById(Long id);

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录集合
     */
    TableDataInfo<PrivateUserVo> queryPageList(PrivateUserBo bo, PageQuery pageQuery);

    /**
     * 查询私信记录列表
     *
     * @param bo 私信记录
     * @return 私信记录集合
     */
    List<PrivateUserVo> queryList(PrivateUserBo bo);

    /**
     * 修改私信记录
     *
     * @param bo 私信记录
     * @return 结果
     */
    Boolean insertByBo(PrivateUserBo bo);

    /**
     * 修改私信记录
     *
     * @param bo 私信记录
     * @return 结果
     */
    Boolean updateByBo(PrivateUserBo bo);

    /**
     * 校验并批量删除私信记录信息
     *
     * @param ids 需要删除的私信记录主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean deleteById(String id);

    Boolean deleteAll();

    boolean addPrivateUser(String targetUid);
}
