package com.aurora.business.domain.entity.configure;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.Date;


/**
 * 工具信息对象 to_tool_info
 *
 * @author aurora
 * @date 2022-10-21
 */
@Data
@TableName("to_tool_info")
public class ToToolInfo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 工具名称
     */
    private String toolName;
    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;
    /**
     * 图标
     */
    private String icon;
    /**
     * 是否为父级（1；父级，2：子级）
     */
    private Integer isParent;
    /**
     * 工具地址
     */
    private String toolUrl;
    /**
     * 排序
     */
    @TableField("`order`")
    private Long order;
    /**
     * 类型
     */
    private Long type;
    /**
     * 状态（0：正常，1：已失效）
     */
    private Integer state;
    /**
     * 创建人
     */
    private String createBy;

    private Date createTime;

}
