package com.aurora.business.domain.vo.dictum;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.aurora.common.annotation.ExcelDictFormat;
import com.aurora.common.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;


/**
 * 名言分组视图对象 b_dictum_group
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ExcelIgnoreUnannotated
public class DictumGroupVo {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ExcelProperty(value = "id")
    private Long id;

    /**
     * 收录数
     */
    private Long employSum;

    /**
     * 分组名称
     */
    @ExcelProperty(value = "分组名称")
    private String name;

    /**
     * 封面
     */
    @ExcelProperty(value = "封面")
    private String cover;

    /**
     * 简介
     */
    @ExcelProperty(value = "简介")
    private String briefIntroduction;

    /**
     * 状态（0：正常吗：已删除）
     */
    @ExcelProperty(value = "状态", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "0=：正常吗：已删除")
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

}

