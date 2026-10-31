package com.qixidi.auth.domain.bo;

import com.light.core.core.domain.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * IP 黑名单查询/新增对象 sys_black_list
 *
 * @author ziwei
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysBlackListBo extends PageQuery {

    /**
     * ip 地址（查询 like；新增时与 uid 至少填一项）
     */
    private String ip;

    /**
     * 归属地（查询 like；新增由后端按 ip 反查，前端不传）
     */
    private String homePlace;

    /**
     * 用户 uid（查询 eq；新增可选）
     */
    private String uid;

    /**
     * 拉黑原因备注（新增可选）
     */
    private String detail;
}
