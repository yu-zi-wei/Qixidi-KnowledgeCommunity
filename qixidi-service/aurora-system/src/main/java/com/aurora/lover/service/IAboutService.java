/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 18:21
 * @version 1.0
 */
package com.aurora.lover.service;

import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.bo.AboutBo;
import com.aurora.lover.domain.vo.AboutVo;

import java.util.Collection;
import java.util.List;

/**
 * 关于我们Service接口
 *
 * @author ziwei
 * @date 2022-11-30
 */
public interface IAboutService {

    /**
     * 查询关于我们
     */
    AboutVo queryById(Long id);

    /**
     * 查询关于我们列表
     */
    TableDataInfo<AboutVo> queryPageList(AboutBo bo, PageQuery pageQuery);

    /**
     * 查询关于我们列表
     */
    List<AboutVo> queryList(AboutBo bo);

    /**
     * 新增关于我们
     */
    Boolean insertByBo(AboutBo bo);

    /**
     * 修改关于我们
     */
    Boolean updateByBo(AboutBo bo);

    /**
     * 校验并批量删除关于我们信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    AboutVo aboutApi();

}
