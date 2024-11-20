package com.aurora.business.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * Software：IntelliJ IDEA 2021.2 x64
 * Date: 2021/9/16 14:47
 * ClassName:SystemMysqlBackups
 * 类描述： MySQL备份实体
 */
@Data
@TableName("mysql_backups")
public class SystemMysqlBackups {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * MySQL服务器IP地址
     */
    private String mysqlIp;

    /**
     * MySQL服务器端口号
     */
    private String mysqlPort;

    /**
     * MySQL服务器端口号
     */
    private String databaseName;

    /**
     * MySQL备份指令
     */
    @TableField("mysql_cmd")
    private String mysqlCmd;

    /**
     * MySQL恢复指令
     */
    private String mysqlBackCmd;

    /**
     * MySQL备份存储地址
     */
    private String backupsPath;

    /**
     * MySQL备份文件名称
     */
    private String backupsName;

    /**
     * 操作次数
     */
    private Integer operation;

    /**
     * 数据状态
     */
    private Integer status;

    /**
     * 恢复时间
     */
    private Date recoveryTime;

    /**
     * 备份时间
     */
    private Date createTime;

}
