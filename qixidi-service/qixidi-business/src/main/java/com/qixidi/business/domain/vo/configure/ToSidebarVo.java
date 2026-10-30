package com.qixidi.business.domain.vo.configure;


import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;

import java.util.List;
import com.qixidi.business.domain.entity.configure.ToSidebar;


/**
 * 侧边栏配置视图对象 to_sidebar
 *
 * @author aurora
 * @date 2022-09-16
 */
@Data
@ExcelIgnoreUnannotated
public class ToSidebarVo extends ToSidebar {

    private static final long serialVersionUID = 1L;


    /**
     * 二级菜单列表
     */
    private List<ToSidebarVo> levelList;

}
