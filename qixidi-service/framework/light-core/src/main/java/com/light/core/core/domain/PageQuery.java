package com.light.core.core.domain;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.light.core.utils.StringUtils;
import com.light.core.utils.sql.SqlUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分页查询实体类
 * <p>
 * BaseEntity 继承本类后，BO/查询实体从请求体直接携带分页参数；
 * 字段不参与 MP 列映射（exist=false），序列化只进不出（WRITE_ONLY）避免污染响应。
 *
 * @author Lion Li
 */
@Data
@NoArgsConstructor
public class PageQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 分页大小
     */
    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer pageSize;

    /**
     * 当前页数
     */
    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer pageNum;

    /**
     * 排序列
     */
    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String orderByColumn;

    /**
     * 排序的方向desc或者asc
     */
    @TableField(exist = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String isAsc;

    /**
     * 当前记录起始索引 默认值
     */
    public static final int DEFAULT_PAGE_NUM = 1;

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final int DEFAULT_PAGE_SIZE = Integer.MAX_VALUE;

    public PageQuery(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public <T> Page<T> build() {
        Integer pageNum = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Integer pageSize = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(pageNum, pageSize);
        OrderItem orderItem = buildOrderItem();
        if (ObjectUtil.isNotNull(orderItem)) {
            page.addOrder(orderItem);
        }
        return page;
    }

    private OrderItem buildOrderItem() {
        // 兼容前端排序类型
        if ("ascending".equals(isAsc)) {
            isAsc = "asc";
        } else if ("descending".equals(isAsc)) {
            isAsc = "desc";
        }
        if (StringUtils.isNotBlank(orderByColumn)) {
            String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
            orderBy = StringUtils.toUnderScoreCase(orderBy);
            if ("asc".equals(isAsc)) {
                return OrderItem.asc(orderBy);
            } else if ("desc".equals(isAsc)) {
                return OrderItem.desc(orderBy);
            }
        }
        return null;
    }

}
