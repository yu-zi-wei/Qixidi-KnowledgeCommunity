package com.qixidi.business.service.shield;

import com.qixidi.business.domain.bo.shield.ToShieldWordBo;
import com.qixidi.business.domain.vo.shield.ToShieldWordVo;
import com.light.core.core.domain.PageQuery;
import com.light.core.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 屏蔽词库Service接口
 *
 * @author aurora
 * @date 2023-02-20
 */
public interface IToShieldWordService {

    /**
     * 查询屏蔽词库
     *
     * @param id 屏蔽词库主键
     * @return 屏蔽词库
     */
    ToShieldWordVo queryById(Long id);

    /**
     * 查询屏蔽词库列表
     *
     * @param bo 屏蔽词库
     * @return 屏蔽词库集合
     */
    TableDataInfo<ToShieldWordVo> queryPageList(ToShieldWordBo bo, PageQuery pageQuery);

    /**
     * 查询屏蔽词库列表
     *
     * @param bo 屏蔽词库
     * @return 屏蔽词库集合
     */
    List<ToShieldWordVo> queryList(ToShieldWordBo bo);

    /**
     * 修改屏蔽词库
     *
     * @param bo 屏蔽词库
     * @return 结果
     */
    Boolean insertByBo(ToShieldWordBo bo);

    /**
     * 修改屏蔽词库
     *
     * @param bo 屏蔽词库
     * @return 结果
     */
    Boolean updateByBo(ToShieldWordBo bo);

    /**
     * 校验并批量删除屏蔽词库信息
     *
     * @param ids     需要删除的屏蔽词库主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return 结果
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void ShieldWordRefresh();

    Map<String, Object> detection(String text);
}
