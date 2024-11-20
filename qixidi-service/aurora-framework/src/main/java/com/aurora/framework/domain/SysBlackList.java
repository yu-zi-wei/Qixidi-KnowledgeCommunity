package com.aurora.framework.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author ziwei
 * @date 2024年09月16日
 */
@Data
@TableName("sys_black_list")
public class SysBlackList {
    private Long id;
    /**
     * ip地址
     */
    private String ip;

    /**
     * 归属地
     */
    private String homePlace;
    /**
     * uid
     */
    private String uid;
    /**
     * 类容
     */
    private String detail;
    /**
     * 创建时间
     */
    private Date createTime;
}
