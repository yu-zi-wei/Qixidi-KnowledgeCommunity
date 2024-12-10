package com.aurora.business.domain.vo.dictum;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 名言信息视图对象 b_dictum_info
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ExcelIgnoreUnannotated
public class DictumInfoVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String content;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String contentMd;

    /**
     * 分类id
     */
    @ExcelProperty(value = "分类id")
    private Long groupId;

    /**
     * 分类名称
     */
    private String groupName;

    /**
     * 专辑id
     */
    private Long albumId;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 作者
     */
    private String author;

    /**
     * 作品名称
     */
    private String worksName;

    /**
     * 标签（多个以逗号隔开）
     */
    @ExcelProperty(value = "标签", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "多=个以逗号隔开")
    private String label;

    /**
     * 标签列表
     */
    private List<String> labelList;

    /**
     * 点赞总数
     */
    @ExcelProperty(value = "点赞总数")
    private Long helpSum;

    /**
     * 评论总数
     */
    @ExcelProperty(value = "评论总数")
    private Long commentSum;

    /**
     * 图片（多个以逗号隔开）
     */
    @ExcelProperty(value = "图片", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "多=个以逗号隔开")
    private String picture;
    /**
     * 图片列表
     */
    private List<String> pictureList;

    /**
     * 名言状态（1：公开，1：私有，3：关注可看）
     */
    @ExcelProperty(value = "名言状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：公开，1：私有，3：关注可看")
    private Integer dictumState;

    /**
     * 状态（0，正常，2：以删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=，正常，2：以删除")
    private Integer state;


    private Date createTime;

    private Date updateTime;

    /**
     * 用户名称
     */
    private String nickname;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 职业
     */
    private String occupation;
    /**
     * 角色（1，普通用户，2，vip用户）
     */
    private String roleId;
    /**
     * 位置
     */
    private String location;

    private Long sum;
}

