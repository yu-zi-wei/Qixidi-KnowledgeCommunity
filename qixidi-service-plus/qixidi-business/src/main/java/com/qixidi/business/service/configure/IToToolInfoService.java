package com.qixidi.business.service.configure;

import com.qixidi.business.domain.bo.configure.ToToolInfoBo;
import com.qixidi.business.domain.vo.configure.ToToolInfoVo;
import com.light.core.core.domain.PageQuery;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 工具信息Service接口
 *
 * @author aurora
 * @date 2022-10-21
 */
public interface IToToolInfoService {

    /**
     * 查询工具信息
     *
     * @param id 工具信息主键
     * @return 工具信息
     */
    ToToolInfoVo queryById(Long id);

    /**
     * 查询工具信息列表
     *
     * @param bo 工具信息
     * @return 工具信息集合
     */
    Map<String, Object> queryPageList(ToToolInfoBo bo, PageQuery pageQuery);

    /**
     * 查询工具信息列表
     *
     * @param bo 工具信息
     * @return 工具信息集合
     */
    List<ToToolInfoVo> queryList(ToToolInfoBo bo);

    /**
     * 修改工具信息
     *
     * @param bo 工具信息
     * @return 结果
     */
    Boolean insertByBo(ToToolInfoBo bo);

    /**
     * 修改工具信息
     *
     * @param bo 工具信息
     * @return 结果
     */
    Boolean updateByBo(ToToolInfoBo bo);

    /**
     * 校验并批量删除工具信息信息
     *
     * @param ids     需要删除的工具信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<ToToolInfoVo> fdList();

    List<ToToolInfoVo> childList(ToToolInfoBo bo);
}

