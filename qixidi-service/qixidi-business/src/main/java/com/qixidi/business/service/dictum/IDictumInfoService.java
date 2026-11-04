package com.qixidi.business.service.dictum;

import com.qixidi.business.domain.bo.dictum.DictumBatchBo;
import com.qixidi.business.domain.bo.dictum.DictumInfoBo;
import com.qixidi.business.domain.entity.dictum.DictumInfo;
import com.qixidi.business.domain.vo.dictum.DictumInfoVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 名言信息Service接口
 *
 * @author aurora
 * @date 2023-04-24
 */
public interface IDictumInfoService {

    /**
     * 查询名言信息
     *
     * @param id 名言信息主键
     * @return 名言信息
     */
    DictumInfoVo queryById(Long id);

    /**
     * 查询名言信息列表
     *
     * @param bo 名言信息
     * @return 名言信息集合
     */
    TableDataInfo<DictumInfoVo> queryPageList(DictumInfoBo bo, PageQuery pageQuery);

    /**
     * 查询名言信息列表
     *
     * @param bo 名言信息
     * @return 名言信息集合
     */
    List<DictumInfoVo> queryList(DictumInfoBo bo);

    /**
     * 修改名言信息
     *
     * @param bo 名言信息
     * @return 结果
     */
    Boolean insertByBo(DictumInfoBo bo);

    /**
     * 批量新增名言信息（一次发布多条，分类/作者等元信息共享）
     *
     * @param bo 批量名言信息
     * @return 结果
     */
    Boolean insertBatchByBo(DictumBatchBo bo);

    /**
     * 修改名言信息
     *
     * @param bo 名言信息
     * @return 结果
     */
    Boolean updateByBo(DictumInfoBo bo);

    /**
     * 校验并批量删除名言信息信息
     *
     * @param ids     需要删除的名言信息主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    boolean deleteWithValidById(Long id, Long groupId);

    /**
     * 按属主 uid + id 查随笔（单表，不存在或非本人返回 null）。
     * MCP agent 更新/删除前校验属主用——updateByBo/deleteWithValidById 无属主校验，
     * queryById 对不存在 id 会 NPE，均不适合直接复用。
     */
    DictumInfo getByUidAndId(String uid, Long id);
}

