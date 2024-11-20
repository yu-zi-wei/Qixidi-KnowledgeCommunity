package com.aurora.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 网站信息表
 *
 * @author ziwei
 * @date 2024年09月16日
 */
@Data
@TableName("to_site_info")
public class ToSiteInfo {

    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 域名
     */
    private String realmName;
    /**
     * 备案
     */
    private String filings;
    /**
     * 公安备案
     */
    private String securityFilings;
    /**
     * 反馈邮箱
     */
    private String mailbox;
    /**
     * 网站创建时间
     */
    private String createTime;
}
