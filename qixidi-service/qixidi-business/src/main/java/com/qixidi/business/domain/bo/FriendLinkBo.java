package com.qixidi.business.domain.bo;

import com.light.core.core.domain.PageQuery;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 友链业务对象 b_friend_link
 *
 * @author ziwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FriendLinkBo extends PageQuery {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = {AddGroup.class, EditGroup.class})
    private String linkName;

    /**
     * 简介
     */
    private String linkIntro;

    /**
     * 头像
     */
    private String linkAvatar;

    /**
     * 地址
     */
    @NotBlank(message = "地址不能为空", groups = {AddGroup.class, EditGroup.class})
    private String linkUrl;

    /**
     * 状态（0：上架，1：下架）
     */
    @NotNull(message = "状态不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer status;

    /**
     * 状态多选筛选（in 查询，后台列表页）
     */
    private List<Integer> statuses;
}
