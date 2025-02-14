/**
 * TODO 类描述
 *
 * @author: collector
 * @createTime: 2022年11月30日 16:26
 * @version 1.0
 */
package com.qixidi.love.domain;


import com.light.core.core.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 爱情祝福语对象 lover_comment
 *
 * @author ziwei
 * @date 2022-11-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lover_comment")
public class LoverComment extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * id
     */
    @TableId(value = "id",type=IdType.AUTO)
    private Long id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String name;
    /**
     * 评论
     */
    private String content;

}
