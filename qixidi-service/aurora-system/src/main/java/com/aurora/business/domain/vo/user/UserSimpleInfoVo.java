package com.aurora.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户 简单的基本信息
 */
@Data
public class UserSimpleInfoVo {

    /**
     * 私信链接 是否在线
     */
    private Boolean isOnline;


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
     * 角色（1，普通用户，2，vip用户）
     */
    @ExcelProperty(value = "角色", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=，普通用户，2，vip用户")
    @ApiModelProperty("角色（1，普通用户，2，vip用户）")
    private Long roleId;

    @ExcelProperty(value = "职业")
    @ApiModelProperty("职业")
    private String occupation;
}
