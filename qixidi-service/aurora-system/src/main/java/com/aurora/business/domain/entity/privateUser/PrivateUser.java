package com.aurora.business.domain.entity.privateUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;


/**
 * 私信记录对象 b_private_user
 *
 * @author aurora
 * @date 2023-03-23
 */
@Data
@TableName("b_private_user")
@Accessors(chain = true)
public class PrivateUser {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户id
     */
    private String uid;
    /**
     * 目标用户id
     */
    private String targetUid;

    /**
     * 最后一条聊天数据
     */
    private String lastNews;

    private Date createTime;

    private Date updateTime;

}
