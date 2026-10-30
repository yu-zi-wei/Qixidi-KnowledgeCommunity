package com.qixidi.business.domain.vo.dictum;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;

import java.util.Date;
import com.qixidi.business.domain.entity.dictum.DictumGroup;


/**
 * 名言分组视图对象 b_dictum_group
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@ExcelIgnoreUnannotated
public class DictumGroupVo extends DictumGroup {

    private static final long serialVersionUID = 1L;

    /**
     * 收录数
     */
    private Long employSum;

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

