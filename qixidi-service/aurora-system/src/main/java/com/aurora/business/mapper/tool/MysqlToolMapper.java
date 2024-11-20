package com.aurora.business.mapper.tool;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MysqlToolMapper {
    @Select("show tables")
    List<String> showTables();

    Integer emptyData(@Param("tableName") String tableName);

    Integer emptyDataList(@Param("yesList") List<String> yesList);

}
