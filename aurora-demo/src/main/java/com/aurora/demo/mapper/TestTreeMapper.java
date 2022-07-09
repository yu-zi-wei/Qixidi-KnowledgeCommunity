package com.aurora.demo.mapper;

import com.aurora.common.annotation.DataColumn;
import com.aurora.common.annotation.DataPermission;
import com.aurora.common.core.mapper.BaseMapperPlus;
import com.aurora.demo.domain.TestTree;
import com.aurora.demo.domain.vo.TestTreeVo;

/**
 * 测试树表Mapper接口
 *
 * @author Lion Li
 * @date 2021-07-26
 */
@DataPermission({
    @DataColumn(key = "deptName", value = "dept_id"),
    @DataColumn(key = "userName", value = "user_id")
})
public interface TestTreeMapper extends BaseMapperPlus<TestTreeMapper, TestTree, TestTreeVo> {

}
