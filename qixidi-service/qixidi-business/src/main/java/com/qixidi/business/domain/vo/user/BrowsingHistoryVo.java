package com.qixidi.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.user.BrowsingHistory;


/**
 * 用户浏览历史
 * <p>
 * 视图对象 b_browsing_history
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ExcelIgnoreUnannotated
public class BrowsingHistoryVo extends BrowsingHistory {

    private static final long serialVersionUID = 1L;

}

