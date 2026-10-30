package com.qixidi.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.configure.ToToolInfo;


/**
 * 工具信息视图对象 to_tool_info
 *
 * @author aurora
 * @date 2022-10-21
 */
@Data
@ExcelIgnoreUnannotated
public class ToToolInfoVo extends ToToolInfo {

    private static final long serialVersionUID = 1L;

}

