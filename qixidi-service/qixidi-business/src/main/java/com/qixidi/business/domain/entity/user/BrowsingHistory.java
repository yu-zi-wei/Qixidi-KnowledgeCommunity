package com.qixidi.business.domain.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;
import com.light.excel.convert.ExcelDictConvert;
import com.alibaba.excel.annotation.ExcelProperty;
import com.light.core.annotation.ExcelDictFormat;


/**
 * 用户浏览历史
 * <p>
 * 对象 b_browsing_history
 *
 * @author aurora
 * @date 2023-04-24
 */
@Data
@TableName("b_browsing_history")
public class BrowsingHistory {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty(value = "id")
    private Long id;
    /**
     * 用户id
     */
    @ExcelProperty(value = "用户id")
    private String uid;
    /**
     * 目标id
     */
    @ExcelProperty(value = "目标id")
    private Long targetId;
    /**
     * 状态
     */
    private Long state;
    /**
     * 目标标题
     */
    @ExcelProperty(value = "目标标题")
    private String targetTitle;
    /**
     * 目标uid
     */
    @ExcelProperty(value = "目标uid")
    private String targetUid;
    /**
     * 目标类型（1：文章，2：小记，3：随笔）
     */
    @ExcelProperty(value = "目标类型", converter = ExcelDictConvert.class)
    @ExcelDictFormat(readConverterExp = "1=：文章，2=：小记，3=：随笔")
    private Integer targetType;

    /**
     * 行为类型（1：阅读，2：点赞，3：评论）——该足迹最近一次由什么行为产生
     */
    @ExcelDictFormat(readConverterExp = "1=：阅读，2=：点赞，3=：评论")
    private Integer actionType;

    /**
     * 评论内容快照（仅评论行为写入；评论后续被删除不影响快照）
     */
    private String content;

    private Date createTime;

    private Date updateTime;

}
