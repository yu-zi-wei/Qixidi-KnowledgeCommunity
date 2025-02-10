package com.light.mybatisPlus.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zi-wei
 * @create 2025/2/5 17:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_sys_menu_hidden")
public class SysMenuHidden {
    /**
     * 对象id（菜单id）
     */
    private Long menuId;

    /**
     * 平台类型
     */
    private String platformType;
    /**
     * 业务类型(参考：MenuFiltrationEnums枚举)
     */
    private String code;

    /**
     * 业务类型的具体值1
     */
    private Integer businessValue;

    /**
     * 描述
     */
    private String description;

}
