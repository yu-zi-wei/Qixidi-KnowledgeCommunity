package com.qixidi.system.domain.vo;

import lombok.Data;

import java.util.Date;
import com.qixidi.system.domain.entity.SysOss;

/**
 * OSS对象存储视图对象 sys_oss
 *
 * @author Lion Li
 */
@Data
public class SysOssVo extends SysOss {

    private static final long serialVersionUID = 1L;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 上传人
     */
    private String createBy;


}
