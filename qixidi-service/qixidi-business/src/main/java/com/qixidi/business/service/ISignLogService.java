package com.qixidi.business.service;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.business.domain.bo.user.SignLogBo;
import com.qixidi.business.domain.entity.user.SignLog;

/**
 * 前台登录日志 Service 接口
 *
 * @author ziwei
 * @date 2026-10-31
 */
public interface ISignLogService {

    /**
     * 记录前台登录/登出日志（b_sign_log）
     * <p>
     * 登录低频、单行插入，同步执行即可；内部吞异常，日志失败不影响登录主流程。
     * IP 须在 HTTP 线程取（RequestContextHolder 是 ThreadLocal，异步线程取不到）
     *
     * @param uid      用户id
     * @param userName 用户名称
     * @param logout   true 登出 / false 登录
     */
    void recordSignLog(String uid, String userName, boolean logout);

    /**
     * 分页查询前台登录日志（userName/ipAddress 模糊、type 精确，id 倒序）
     */
    TableDataInfo<SignLog> queryPageList(SignLogBo bo, PageQuery pageQuery);

    /**
     * 批量删除前台登录日志
     */
    void deleteSignLogByIds(Long[] ids);

    /**
     * 清空前台登录日志
     */
    void cleanSignLog();

}
