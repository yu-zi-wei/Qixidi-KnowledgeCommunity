package com.qixidi.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.configure.ToSiteFile;


/**
 * 网站文档视图对象 to_site_file
 *
 * @author aurora
 * @date 2022-10-21
 */
@Data
@ExcelIgnoreUnannotated
public class ToSiteFileVo extends ToSiteFile {

    private static final long serialVersionUID = 1L;

}
