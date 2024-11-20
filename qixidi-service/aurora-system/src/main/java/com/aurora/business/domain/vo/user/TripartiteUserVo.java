package com.aurora.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.business.domain.entity.count.CountUserWebsiteEntity;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * 【请填写功能名称】视图对象 tripartite_user
 *
 * @author ruoyi
 * @date 2022-06-12
 */
@Data
@ApiModel("【请填写功能名称】视图对象")
@ExcelIgnoreUnannotated
public class TripartiteUserVo extends CountUserWebsiteEntity {

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    @ApiModelProperty("用户名")
    private String username;

    /**
     * 是否关注
     */
    private Boolean isFollow = false;

    /**
     * 关注按钮状态
     */
    private Boolean buttonLoading = false;

    /**
     * 用户昵称
     */
    @ExcelProperty(value = "用户昵称")
    @ApiModelProperty("用户昵称")
    private String nickname;

    /**
     * A 币数量
     */
    private Long aCurrency;


    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码")
    @ApiModelProperty("密码")
    private String password;

    /**
     * 盐
     */
    @ExcelProperty(value = "盐")
    @ApiModelProperty("盐")
    private String salt;

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
    @ExcelProperty(value = "用户类型")
    @ApiModelProperty("用户类型")
    private String userType;

    /**
     * 用户状态（0：正常，1：冻结，2：已删除）
     */
    @ExcelProperty(value = "用户状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常，1：冻结，2：已删除")
    @ApiModelProperty("用户状态（0：正常，1：冻结，2：已删除）")
    private Integer state;

    /**
     * 最后登录ip
     */
    @ExcelProperty(value = "最后登录ip")
    @ApiModelProperty("最后登录ip")
    private String loginIp;

    /**
     * 角色（1，普通用户，2，vip用户）
     */
    @ExcelProperty(value = "角色", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=，普通用户，2，vip用户")
    @ApiModelProperty("角色（1，普通用户，2，vip用户）")
    private Long roleId;

    @ExcelProperty(value = "职业")
    @ApiModelProperty("职业")
    private String occupation;

    /**
     * 个人简介
     */
    @ApiModelProperty("个人简介")
    private String introduce;

    /**
     * 个人主页地址
     */
    @ApiModelProperty("个人主页地址")
    private String homepage;
    /**
     * 用户经验值
     */
    @ApiModelProperty("用户经验值")
    private Integer empirical;

    @ApiModelProperty("用户等级")
    private String grade;

    @ApiModelProperty("用户状态（0：正常，1：已删除，2：冻结，）")
    private Integer userState;

    /**
     * 时间
     */
    private Date createTime;

}
