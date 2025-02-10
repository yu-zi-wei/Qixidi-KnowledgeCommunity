package com.qixidi.business.domain.bo.dictum;

import com.light.core.core.domain.BaseEntity;
import com.light.core.core.validate.AddGroup;
import com.light.core.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


/**
 * 名言信息业务对象 b_dictum_info
 *
 * @author aurora
 * @date 2023-04-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class DictumInfoBo extends BaseEntity {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 内容
     */
    @NotBlank(message = "名言不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    private String contentMd;

    /**
     * 分类id
     */
    @NotNull(message = "分类不能为空", groups = {AddGroup.class, EditGroup.class})
    private Long groupId;

    /**
     * 专辑id
     */
    private Long albumId;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 标签（多个以逗号隔开）
     */
    private String label;

    /**
     * 点赞总数
     */
    private Long helpSum;

    /**
     * 评论总数
     */
    private Long commentSum;

    /**
     * 图片（多个以逗号隔开）
     */
    private String picture;

    /**
     * 作者
     */
    private String author;

    /**
     * 作品名称
     */
    private String worksName;


    /**
     * 名言状态（1：公开，1：私有，3：关注可看）
     */
    @NotNull(message = "名言状态（1：公开，1：私有，3：关注可看）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer dictumState;

    /**
     * 状态（0，正常，2：以删除）
     */
    private Integer state;

    /**
     * 用户名称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 角色（1，普通用户，2，vip用户）
     */
    private String roleId;
    /**
     * 位置
     */
    private String location;

}

