package com.aurora.business.service.stat;

import com.aurora.business.domain.vo.stat.StatDataInfoVo;
import com.aurora.business.domain.vo.stat.StatReturnDataVo;

import java.util.List;
import java.util.Map;

/**
 * 统计数据信息Service接口
 *
 * @author aurora
 * @date 2023-03-14
 */
public interface IStatDataInfoService {

    StatDataInfoVo queryPageList(StatDataInfoVo bo);

    List<StatReturnDataVo> theList(StatDataInfoVo bo);

    Map<String, Object> labelDate(StatDataInfoVo bo);
}
