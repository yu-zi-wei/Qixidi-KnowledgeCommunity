package com.qixidi.business.domain.entity.configure;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


/**
 * 网站文档对象 to_site_file
 *
 * @author aurora
 * @date 2022-10-21
 */
@Data
@TableName("to_site_file")
public class ToSiteFile {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 创建人
     */
    private Long uid;
    /**
     * 状态（0：正常，1：已失效）
     */
    private Integer state;
    /**
     * 类型
     */
    private String type;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 主题
     */
    private String theme;
}

