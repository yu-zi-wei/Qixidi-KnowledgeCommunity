package com.qixidi.business.domain.bo.news;

import com.light.core.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * 系统消息业务对象 b_news_system_info
 *
 * @author aurora
 * @date 2023-04-23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NewsSystemInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 消息标题
     */
    @NotBlank(message = "消息标题不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newsTitle;

    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空", groups = {AddGroup.class, EditGroup.class})
    private String newsContent;

    /**
     * 是否有详情（1：无，2：有）；可选，空时 Service 默认 1（门户无消费）
     */
    private Long isDetails;

    /**
     * 状态
     */
    private Long state;

    /**
     * 是否群发（1：群发，2：指定人发）；可选，空时 Service 默认 1 群发；指定人发须传 uid
     */
    private Long isMassAir;

    /**
     * 指定人发的接收人 uuid（isMassAir=2 时必填）
     */
    private String uid;

    /**
     * 消息类型（1：系统，2：用户）；可选，Service 固化 2（前台消费链路只认 type=2）
     */
    private Long type;

    /**
     * 失效时间
     */
    private Date lapseTime;

    /**
     * 发送方式多选筛选（列表查询用）
     */
    private List<Long> isMassAirs;

    /**
     * 状态多选筛选（列表查询用）
     */
    private List<Long> states;

}
