package com.qixidi.business.domain.vo.configure;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.util.List;
import com.qixidi.business.domain.entity.configure.ToNavigation;


/**
 * 导航栏配置视图对象 to_navigation
 *
 * @author aurora
 * @date 2022-09-16
 */
@Data
@ExcelIgnoreUnannotated
public class ToNavigationVo extends ToNavigation {

    private static final long serialVersionUID = 1L;

    /**
     * 二级菜单列表
     */
    private List<ToNavigationVo> levelList;
}
