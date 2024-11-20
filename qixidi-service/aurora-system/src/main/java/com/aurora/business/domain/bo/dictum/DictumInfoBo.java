package com.aurora.business.domain.bo.dictum;

import com.aurora.common.core.domain.BaseEntity;
import com.aurora.common.core.validate.AddGroup;
import com.aurora.common.core.validate.EditGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 名言信息业务对象 dictum_info
 *
 * @author aurora
 * @date 2023-04-24
 */

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("名言信息业务对象")
public class DictumInfoBo extends BaseEntity {

    /**
     * id
     */
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private String uid;

    /**
     * 内容
     */
    @ApiModelProperty(value = "名言", required = true)
    @NotBlank(message = "名言不能为空", groups = {AddGroup.class, EditGroup.class})
    private String content;

    @ApiModelProperty(value = "名言", required = true)
    private String contentMd;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id", required = true)
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
    @ApiModelProperty(value = "标签（多个以逗号隔开）", required = true)
    private String label;

    /**
     * 点赞总数
     */
    @ApiModelProperty(value = "点赞总数", required = true)
    private Long helpSum;

    /**
     * 评论总数
     */
    @ApiModelProperty(value = "评论总数", required = true)
    private Long commentSum;

    /**
     * 图片（多个以逗号隔开）
     */
    @ApiModelProperty(value = "图片（多个以逗号隔开）", required = true)
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
    @ApiModelProperty(value = "名言状态（1：公开，1：私有，3：关注可看）", required = true)
    @NotNull(message = "名言状态（1：公开，1：私有，3：关注可看）不能为空", groups = {AddGroup.class, EditGroup.class})
    private Integer dictumState;

    /**
     * 状态（0，正常，2：以删除）
     */
    @ApiModelProperty(value = "状态（0，正常，2：以删除）", required = true)
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

