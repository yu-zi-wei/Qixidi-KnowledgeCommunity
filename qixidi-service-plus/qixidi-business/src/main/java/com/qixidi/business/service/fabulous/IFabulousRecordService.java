package com.qixidi.business.service.fabulous;

import com.qixidi.business.domain.bo.fabulous.FabulousRecordBo;
import com.qixidi.business.domain.bo.user.UserHomeBo;
import com.qixidi.business.domain.vo.fabulous.FabulousRecordVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.domain.R;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 点赞Service接口
 *
 * @author aurora
 * @date 2022-10-01
 */
public interface IFabulousRecordService {

    /**
     * 查询点赞
     *
     * @param id 点赞主键
     * @return 点赞
     */
    FabulousRecordVo queryById(Long id);

    /**
     * 查询点赞列表
     *
     * @param bo 点赞
     * @return 点赞集合
     */
    TableDataInfo<FabulousRecordVo> queryPageList(FabulousRecordBo bo, PageQuery pageQuery);

    /**
     * 查询点赞列表
     *
     * @param bo 点赞
     * @return 点赞集合
     */
    List<FabulousRecordVo> queryList(FabulousRecordBo bo);

    /**
     * 修改点赞
     *
     * @param bo 点赞
     * @return 结果
     */
    Boolean insertByBo(FabulousRecordBo bo);

    /**
     * 修改点赞
     *
     * @param bo 点赞
     * @return 结果
     */
    Boolean updateByBo(FabulousRecordBo bo);

    /**
     * 校验并批量删除点赞信息
     *
     * @param ids 需要删除的点赞主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    R spotFabulous(FabulousRecordBo bo);

    void cancelFabulous(FabulousRecordBo bo);

    Object fabulousList(UserHomeBo bo, PageQuery pageQuery);

}
