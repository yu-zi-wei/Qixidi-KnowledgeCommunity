/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月21日 14:57
 * @version 1.0
 */
package com.aurora.lover.service;

import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.bo.BasicInfoBo;
import com.aurora.lover.domain.vo.BasicInfoVo;

import java.util.Collection;
import java.util.List;


/**
 * 网站基本信息Service接口
 *
 * @author ruoyi
 * @date 2022-11-21
 */
public interface IBasicInfoService {

    /**
     * 查询网站基本信息
     */
    BasicInfoVo queryById(Long id);

    /**
     * 查询网站基本信息列表
     */
    TableDataInfo<BasicInfoVo> queryPageList(BasicInfoBo bo, PageQuery pageQuery);

    /**
     * 查询网站基本信息列表
     */
    List<BasicInfoVo> queryList(BasicInfoBo bo);

    /**
     * 新增网站基本信息
     */
    Boolean insertByBo(BasicInfoBo bo);

    /**
     * 修改网站基本信息
     */
    Boolean updateByBo(BasicInfoBo bo);

    /**
     * 校验并批量删除网站基本信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    BasicInfoVo basicInfoApi(BasicInfoBo bo);

}
