package com.qixidi.business.service.stat;

import com.qixidi.business.domain.vo.stat.StatDataInfoVo;
import com.qixidi.business.domain.vo.stat.StatReturnDataVo;

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
