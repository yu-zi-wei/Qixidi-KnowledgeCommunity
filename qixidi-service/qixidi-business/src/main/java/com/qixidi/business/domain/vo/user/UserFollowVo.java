package com.qixidi.business.domain.vo.user;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.light.excel.convert.ExcelDictConvert;
import lombok.Data;
import com.qixidi.business.domain.entity.user.UserFollow;


/**
 * 用户关注视图对象 b_user_follow
 *
 * @author aurora
 * @date 2023-02-13
 */
@Data
@ExcelIgnoreUnannotated
public class UserFollowVo extends UserFollow {

    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private Integer sum;


}

