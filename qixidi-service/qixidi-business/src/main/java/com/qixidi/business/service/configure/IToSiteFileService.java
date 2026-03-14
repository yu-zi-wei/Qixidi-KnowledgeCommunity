package com.qixidi.business.service.configure;


import com.qixidi.business.domain.bo.configure.ToSiteFileBo;
import com.qixidi.business.domain.vo.configure.ToSiteFileVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 网站文档Service接口
 *
 * @author aurora
 * @date 2022-10-21
 */
public interface IToSiteFileService {

    /**
     * 查询网站文档
     *
     * @param id 网站文档主键
     * @return 网站文档
     */
    ToSiteFileVo queryById(Long id);

    /**
     * 查询网站文档列表
     *
     * @param bo 网站文档
     * @return 网站文档集合
     */
    TableDataInfo<ToSiteFileVo> queryPageList(ToSiteFileBo bo, PageQuery pageQuery);

    /**
     * 查询网站文档列表
     *
     * @param bo 网站文档
     * @return 网站文档集合
     */
    List<ToSiteFileVo> queryList(ToSiteFileBo bo);

    /**
     * 修改网站文档
     *
     * @param bo 网站文档
     * @return 结果
     */
    Boolean insertByBo(ToSiteFileBo bo);

    /**
     * 修改网站文档
     *
     * @param bo 网站文档
     * @return 结果
     */
    Boolean updateByBo(ToSiteFileBo bo);

    /**
     * 校验并批量删除网站文档信息
     *
     * @param ids     需要删除的网站文档主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}

