/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月29日 9:17
 * @version 1.0
 */
package com.aurora.lover.service;


import com.aurora.common.core.domain.PageQuery;
import com.aurora.common.core.page.TableDataInfo;
import com.aurora.lover.domain.bo.LoverCarouselBo;
import com.aurora.lover.domain.vo.LoverCarouselVo;

import java.util.Collection;
import java.util.List;

/**
 * 爱情轮播图Service接口
 *
 * @author ruoyi
 * @date 2022-11-29
 */
public interface ILoverCarouselService {

    /**
     * 查询爱情轮播图
     */
    LoverCarouselVo queryById(Long id);

    /**
     * 查询爱情轮播图列表
     */
    TableDataInfo<LoverCarouselVo> queryPageList(LoverCarouselBo bo, PageQuery pageQuery);

    /**
     * 查询爱情轮播图列表
     */
    List<LoverCarouselVo> queryList(LoverCarouselBo bo);

    /**
     * 新增爱情轮播图
     */
    Boolean insertByBo(LoverCarouselBo bo);

    /**
     * 修改爱情轮播图
     */
    Boolean updateByBo(LoverCarouselBo bo);

    /**
     * 校验并批量删除爱情轮播图信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<LoverCarouselVo> carouselApi(LoverCarouselBo bo, PageQuery pageQuery);
}
