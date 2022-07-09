package com.aurora.common.core.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 【请填写功能名称】视图对象 tripartite_user
 *
 * @author ruoyi
 * @date 2022-06-12
 */
@Data
@ApiModel("【请填写功能名称】视图对象")
@ExcelIgnoreUnannotated
public class TripartiteUserVo {

    private static final long serialVersionUID = 1L;

    /**
     * 用户第三方系统的唯一id
     */
    @ExcelProperty(value = "用户第三方系统的唯一id")
    @ApiModelProperty("用户第三方系统的唯一id")
    private String uuid;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * 用户头像
     */
    @ExcelProperty(value = "用户头像")
    @ApiModelProperty("用户头像")
    private String avatar;

    /**
     * 用户网址
     */
    @ExcelProperty(value = "用户网址")
    @ApiModelProperty("用户网址")
    private String blog;

    /**
     * 所在公司
     */
    @ExcelProperty(value = "所在公司")
    @ApiModelProperty("所在公司")
    private String company;

    /**
     * 位置
     */
    @ExcelProperty(value = "位置")
    @ApiModelProperty("位置")
    private String location;

    /**
     * 用户邮箱
     */
    @ExcelProperty(value = "用户邮箱")
    @ApiModelProperty("用户邮箱")
    private String email;

    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    @ApiModelProperty("性别")
    private String gender;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    @ExcelProperty(value = "用户备注", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "各=平台中的用户个人介绍")
    @ApiModelProperty("用户备注（各平台中的用户个人介绍）")
    private String remark;

    /**
     * 用户来源
     */
    @ExcelProperty(value = "用户来源")
    @ApiModelProperty("用户来源")
    private String source;

    /**
     * 用户类型
     */
    private String userType;

}
