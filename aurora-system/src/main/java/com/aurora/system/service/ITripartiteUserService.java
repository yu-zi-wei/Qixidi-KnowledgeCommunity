package com.aurora.system.service;


import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.domain.bo.TripartiteUserBo;
import com.aurora.common.core.domain.entity.TripartiteUser;
import com.aurora.common.core.domain.vo.TripartiteUserVo;
import com.aurora.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 【请填写功能名称】Service接口
 *
 * @author ruoyi
 * @date 2022-06-12
 */
public interface ITripartiteUserService {

    /**
     * 查询【请填写功能名称】
     *
     * @param uuid 【请填写功能名称】主键
     * @return 【请填写功能名称】
     */
    TripartiteUserVo queryById(String uuid);

    /**
     * 查询【请填写功能名称】列表
     *
     * @return 【请填写功能名称】集合
     */
    TableDataInfo<TripartiteUserVo> queryPageList(TripartiteUserBo bo, PageQuery pageQuery);

    /**
     * 查询【请填写功能名称】列表
     *
     * @return 【请填写功能名称】集合
     */
    List<TripartiteUserVo> queryList(TripartiteUserBo bo);

    /**
     * 修改【请填写功能名称】
     *
     * @return 结果
     */
    Boolean insertByBo(TripartiteUserBo bo);

    /**
     * 修改【请填写功能名称】
     *
     * @return 结果
     */
    Boolean updateByBo(TripartiteUserBo bo);

    /**
     * 校验并批量删除【请填写功能名称】信息
     *
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<String> ids, Boolean isValid);

    void oauthLogin(TripartiteUser tripartiteUser);
}
