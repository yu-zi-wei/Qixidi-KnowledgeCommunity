package com.qixidi.business.domain.vo.news;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.news.NewsSystemInfo;


/**
 * 系统消息视图对象 b_news_system_info
 *
 * @author aurora
 * @date 2023-04-23
 */
@Data
@ExcelIgnoreUnannotated
public class NewsSystemInfoVo extends NewsSystemInfo {

    private static final long serialVersionUID = 1L;


}

