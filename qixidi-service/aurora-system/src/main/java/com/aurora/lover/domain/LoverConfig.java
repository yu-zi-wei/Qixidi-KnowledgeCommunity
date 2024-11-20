package com.aurora.lover.domain;

import com.aurora.common.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 基本配置对象 lover_congfig
 *
 * @author ruoyi
 * @date 2022-12-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lover_config")
public class LoverConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * is
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 邮箱（多个以逗号隔开）
     */
    private String mail;
    /**
     * 备案号
     */
    private String filings;
    /**
     * 域名
     */
    private String realmName;

}
