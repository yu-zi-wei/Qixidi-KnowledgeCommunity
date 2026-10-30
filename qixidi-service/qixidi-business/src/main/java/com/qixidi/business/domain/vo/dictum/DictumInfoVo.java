package com.qixidi.business.domain.vo.dictum;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.qixidi.business.domain.vo.user.TripartiteUserVo;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import java.util.List;
import com.qixidi.business.domain.entity.dictum.DictumInfo;


/**
 * 名言信息视图对象 b_dictum_info
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ExcelIgnoreUnannotated
public class DictumInfoVo extends DictumInfo {

    private static final long serialVersionUID = 1L;

    /**
     * 内容
     */
    @ExcelProperty(value = "内容")
    private String contentMd;

    /**
     * 分类名称
     */
    private String groupName;

    /**
     * 专辑名称
     */
    private String albumName;

    /**
     * 标签列表
     */
    private List<String> labelList;

    /**
     * 图片列表
     */
    private List<String> pictureList;


    /**
     * 名言总数
     */
    private Long sum;

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
    private Long roleId;
    /**
     * 位置
     */
    private String location;

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
     * 用户详情信息
     */
    private TripartiteUserVo tripartiteUser;
}

