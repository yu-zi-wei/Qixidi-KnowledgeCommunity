package com.aurora.business.service.user;

import com.aurora.business.domain.bo.user.UserReportBo;
import com.aurora.business.domain.vo.user.UserReportVo;
import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户签到Service接口
 *
 * @author aurora
 * @date 2023-04-10
 */
public interface IUserReportService {

    /**
     * 查询用户签到
     *
     * @param id 用户签到主键
     * @return 用户签到
     */
    UserReportVo queryById(Long id);

    /**
     * 查询用户签到列表
     *
     * @param bo 用户签到
     * @return 用户签到集合
     */
    TableDataInfo<UserReportVo> queryPageList(UserReportBo bo, PageQuery pageQuery);

    /**
     * 查询用户签到列表
     *
     * @param bo 用户签到
     * @return 用户签到集合
     */
    List<UserReportVo> queryList(UserReportBo bo);

    /**
     * 修改用户签到
     *
     * @param bo 用户签到
     * @return 结果
     */
    Boolean insertByBo(UserReportBo bo) throws Exception;

    /**
     * 修改用户签到
     *
     * @param bo 用户签到
     * @return 结果
     */
    Boolean updateByBo(UserReportBo bo);

    /**
     * 校验并批量删除用户签到信息
     *
     * @param ids     需要删除的用户签到主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Map<String, Object> List();
}

