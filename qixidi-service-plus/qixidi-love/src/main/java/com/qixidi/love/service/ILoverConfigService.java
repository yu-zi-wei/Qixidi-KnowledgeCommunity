package com.qixidi.love.service;


import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;
import com.qixidi.love.domain.bo.LoverConfigBo;
import com.qixidi.love.domain.vo.LoverConfigVo;

import java.util.Collection;
import java.util.List;

/**
 * 基本配置Service接口
 *
 * @author ziwei
 * @date 2022-12-02
 */
public interface ILoverConfigService {

    /**
     * 查询基本配置
     */
    LoverConfigVo queryById(Long id);

    /**
     * 查询基本配置列表
     */
    TableDataInfo<LoverConfigVo> queryPageList(LoverConfigBo bo, PageQuery pageQuery);

    /**
     * 查询基本配置列表
     */
    List<LoverConfigVo> queryList(LoverConfigBo bo);

    /**
     * 新增基本配置
     */
    Boolean insertByBo(LoverConfigBo bo);

    /**
     * 修改基本配置
     */
    Boolean updateByBo(LoverConfigBo bo);

    /**
     * 校验并批量删除基本配置信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void initializationMail();

    LoverConfigVo configInfo();

}

